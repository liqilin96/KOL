package cn.weihu.kol.job;

import cn.hutool.core.date.DateUtil;
import cn.weihu.kol.biz.MessageBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.db.po.Message;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MonitorJob {

    @Value("${price.expire.remind:30}")
    private Integer expireRemind;

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
            // 创建保价到期项目
            log.info(">>> 保价即将到期项目已生成...");
            // 生成保价到期询价工单
            WorkOrder workOrder = new WorkOrder();
            workOrder.setProjectId(1L);
            workOrder.setProjectName("保价即将到期");
            Long workOrderId = workOrderBiz.create(workOrder, Constants.WORK_ORDER_EXPIRE_DEMAND, Constants.WORK_ORDER_ASK);
            log.info(">>> 保价即将到期工单已生成...");
            // 生成工单数据及提醒消息
            List<WorkOrderData> workOrderDataList = new ArrayList<>();
            List<Message>       messageList       = new ArrayList<>();
            WorkOrderData       workOrderData;
            Message             message;
            for(Prices prices : list) {
                workOrderData = new WorkOrderData();
                workOrderData.setFieldsId(1L);
                workOrderData.setProjectId(1L);
                workOrderData.setWorkOrderId(workOrderId);
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_PRICE);
                workOrderData.setData(prices.getActorData());
                workOrderData.setCtime(DateUtil.date());
                workOrderData.setUtime(DateUtil.date());
                workOrderDataList.add(workOrderData);

                message = new Message();
                messageList.add(message);
            }
            workOrderDataBiz.saveBatch(workOrderDataList);
            log.info(">>> 保价即将到期工单数据已处理...");
            //
            log.info(">>> 保价即将到期询价子工单已生成...");
            messageBiz.saveBatch(messageList);
            log.info(">>> 保价即将到期提醒消息已生成...");
        } else {
            log.info(">>> 不存在一个月后保价到期数据...");
        }
    }
}
