package cn.weihu.kol.job;

import cn.hutool.core.date.DateUtil;
import cn.weihu.kol.biz.*;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.db.po.*;
import cn.weihu.kol.runner.StartupRunner;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class MonitorJob {

    @Value("${price.expire.remind:30}")
    private Integer expireRemind;

    @Autowired
    private ProjectBiz       projectBiz;
    @Autowired
    private PricesBiz        pricesBiz;
    @Autowired
    private WorkOrderBiz     workOrderBiz;
    @Autowired
    private WorkOrderDataBiz workOrderDataBiz;
    @Autowired
    private MessageBiz       messageBiz;

    @Scheduled(cron = "0 0 0 * * ?")
    public void priceExpire() {
        // 保价到期检测
        // 提前一个月
        Date compareTime = DateUtil.offsetDay(DateUtil.date(), -expireRemind);
        List<Prices> list = pricesBiz.list(new LambdaQueryWrapper<>(Prices.class)
                                                   .ge(Prices::getInsureEndtime, compareTime));
        if(!CollectionUtils.isEmpty(list)) {
            log.info(">>> 存在一个月后保价到期数据...size:{}", list.size());
            // 创建保价即将到期项目
            String  name    = "保价即将到期";
            Project project = projectBiz.getOne(new LambdaQueryWrapper<>(Project.class).eq(Project::getName, name));
            if(Objects.isNull(project)) {
                project = new Project();
                project.setName(name);
                project.setDescs("保价即将到期项目");
                project.setCtime(LocalDateTime.now());
                project.setUtime(LocalDateTime.now());
                projectBiz.save(project);
            }
            log.info(">>> 保价即将到期项目已生成...");
            // 生成保价到期询价工单
            WorkOrder workOrder = new WorkOrder();
            workOrder.setProjectId(project.getId());
            workOrder.setProjectName(project.getName());
            workOrder = workOrderBiz.create(workOrder, Constants.WORK_ORDER_EXPIRE_DEMAND, Constants.WORK_ORDER_ASK);
            log.info(">>> 保价即将到期工单已生成...");
            // 生成工单数据及提醒消息
            List<WorkOrderData> workOrderDataList = new ArrayList<>();
            List<Message>       messageList       = new ArrayList<>();
            WorkOrderData       workOrderData;
            Message             message;
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map;
            boolean             existXinYi = false;
            boolean             existWeiGe = false;
            for(Prices prices : list) {
                workOrderData = new WorkOrderData();
                workOrderData.setFieldsId(1L);
                workOrderData.setProjectId(project.getId());
                workOrderData.setWorkOrderId(workOrder.getId());
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_PRICE);
                workOrderData.setData(prices.getActorData());
                workOrderData.setCtime(DateUtil.date());
                workOrderData.setUtime(DateUtil.date());
                workOrderDataList.add(workOrderData);

                map = GsonUtils.gson.fromJson(prices.getActorData(), type);
                message = new Message();
                message.setMessage(StringUtils.join(map.get(Constants.TITLE_MEDIA),
                                                    "-",
                                                    map.get(Constants.TITLE_ACCOUNT),
                                                    "-",
                                                    map.get(Constants.TITLE_RESOURCE_LOCATION),
                                                    ",保价即将到期"));
                message.setType("1");
                if(Constants.SUPPLIER_XIN_YI.equals(map.get(Constants.ACTOR_PROVIDER))) {
                    existXinYi = true;
                    message.setToUserId(StartupRunner.SUPPLIER_USER_XIN_YI);
                }
                if(Constants.SUPPLIER_WEI_GE.equals(map.get(Constants.ACTOR_PROVIDER))) {
                    existWeiGe = true;
                    message.setToUserId(StartupRunner.SUPPLIER_USER_WEI_GE);
                }
                message.setIsReceived(0);
                message.setCtime(LocalDateTime.now());
                message.setUtime(LocalDateTime.now());
                messageList.add(message);
            }
            workOrderDataBiz.saveBatch(workOrderDataList);
            log.info(">>> 保价即将到期工单数据已处理...");
            //
            if(existXinYi) {
                WorkOrder workOrderXinYi = new WorkOrder();
                workOrderXinYi.setOrderSn(workOrder.getOrderSn());
                workOrderXinYi.setName(workOrder.getName());
                workOrderXinYi.setType(Constants.WORK_ORDER_ENQUIRY);
                workOrderXinYi.setProjectId(workOrder.getProjectId());
                workOrderXinYi.setProjectName(workOrder.getProjectName());
                workOrderXinYi.setParentId(workOrder.getId());
                workOrderXinYi.setStatus(Constants.WORK_ORDER_ASK);
                workOrderXinYi.setToUser(StartupRunner.SUPPLIER_USER_XIN_YI);
                workOrderXinYi.setCtime(DateUtil.date());
                workOrderXinYi.setUtime(DateUtil.date());
                workOrderBiz.save(workOrderXinYi);
                log.info(">>> 保价即将到期新意询价子工单已生成...");
            }
            if(existWeiGe) {
                WorkOrder workOrderWeiGe = new WorkOrder();
                workOrderWeiGe.setOrderSn(workOrder.getOrderSn());
                workOrderWeiGe.setName(workOrder.getName());
                workOrderWeiGe.setType(Constants.WORK_ORDER_ENQUIRY);
                workOrderWeiGe.setProjectId(workOrder.getProjectId());
                workOrderWeiGe.setProjectName(workOrder.getProjectName());
                workOrderWeiGe.setParentId(workOrder.getId());
                workOrderWeiGe.setStatus(Constants.WORK_ORDER_ASK);
                workOrderWeiGe.setToUser(StartupRunner.SUPPLIER_USER_XIN_YI);
                workOrderWeiGe.setCtime(DateUtil.date());
                workOrderWeiGe.setUtime(DateUtil.date());
                workOrderBiz.save(workOrderWeiGe);
                log.info(">>> 保价即将到期微格询价子工单已生成...");
            }
            messageBiz.saveBatch(messageList);
            log.info(">>> 保价即将到期提醒消息已生成...");
        } else {
            log.info(">>> 不存在一个月后保价到期数据...");
        }
    }
}
