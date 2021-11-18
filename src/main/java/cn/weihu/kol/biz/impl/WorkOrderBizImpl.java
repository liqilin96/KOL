package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.ProjectBiz;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.convert.WorkOrderConverter;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Project;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class WorkOrderBizImpl extends ServiceImpl<WorkOrderDao, WorkOrder> implements WorkOrderBiz {


    @Autowired
    private WorkOrderBiz workOrderBiz;

    @Autowired
    private WorkOrderDataBiz workOrderDataBiz;

    @Autowired
    private FieldsBiz fieldsBiz;

    @Autowired
    private ProjectBiz projectBiz;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response) {

        WorkOrder workOrder = new WorkOrder();

        try {
            //校验文件类型
            if(!file.getOriginalFilename().endsWith("xls") && !file.getOriginalFilename().endsWith("xlsx")) {
                log.error(file.getOriginalFilename() + "不是excel文件");
                throw new IOException(file.getOriginalFilename() + "不是excel文件");
            }

            List<Object> orderBos = EasyExcelUtil.readExcel(file.getInputStream());

            String        name          = "示例数据";
            WorkOrderData workOrderData = null;

            if(orderBos != null) {
                String uuid = UUID.randomUUID().toString();
                workOrder.setOrderSn(uuid);
                workOrder.setName(req.getName());
                //1是需求工单
                workOrder.setType(1);
                workOrder.setProjectId(Long.parseLong(req.getProjectId()));
                Project project = projectBiz.getById(req.getProjectId());
                workOrder.setProjectName(project.getName());
                workOrder.setCtime(new Date());
                workOrder.setUtime(new Date());
                workOrder.setCreateUserId(UserInfoContext.getUserId());
                workOrder.setUpdateUserId(UserInfoContext.getUserId());

                workOrderBiz.save(workOrder);
            }
            for(Object orderBo : orderBos) {
                LinkedHashMap<Integer, String> bo = (LinkedHashMap<Integer, String>) orderBo;
                /**具体配置如下
                 *
                 *  0     -----》序号
                 *  1     -----》媒体
                 *  2     -----》账号
                 *  3     -----》账号Id或链接
                 *  4     -----》账号类型
                 *  5     -----》粉丝数
                 *  6     -----》资源位置
                 *  7     -----》数量
                 *  8     -----》发布开始时间
                 *  9     -----》发布结束时间
                 *  10    -----》发布结束时间
                 *  11    -----》含电商链接单价
                 *  12    -----》@
                 *  13    -----》话题
                 *  14    -----》电商肖像授权
                 *  15    -----》品牌双微转发授权
                 *  16    -----》微任务（针对微博）
                 *  17    -----》其他特殊说明
                 *  18    -----》产品提供方
                 *  19    -----》发布内容brief概述
                 *
                 */
                //获取第一条数据的账号，如果等于示例数据则跳过
                String accountName = bo.get(2);
                if(name.equalsIgnoreCase(accountName)) {
                    continue;
                }
                workOrderData = new WorkOrderData();

                workOrderData.setProjectId(workOrder.getProjectId());
                workOrderData.setWorkOrderId(workOrder.getId());
                //字段组2为工单
                workOrderData.setFieldsId(2L);

                Fields fields = fieldsBiz.getById(2);
                //获取字段列表
                List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
                }.getType());

                Map<String, String> map   = new HashMap<>();
                List<String>        title = excelTitle();
                for(int i = 0; i < fieldsBos.size(); i++) {
                    for(int j = 0; j < title.size(); j++) {
                        if(title.get(j).equalsIgnoreCase(fieldsBos.get(i).getTitle())) {
                            map.put(fieldsBos.get(i).getDataIndex(), bo.get(j));
                        }
                    }
//                    if(title.contains(fieldsBos.get(i).getTitle())) {
//                        map.put(fieldsBos.get(i).getDataIndex(), bo.get(i));
//                    }
                }
                workOrderData.setData(GsonUtils.gson.toJson(map));
                workOrderData.setCtime(new Date());
                workOrderData.setUtime(new Date());
                workOrderData.setCreateUserId(UserInfoContext.getUserId());
                workOrderData.setUpdateUserId(UserInfoContext.getUserId());
                workOrderDataBiz.save(workOrderData);
            }

        } catch(Exception e) {
            throw new CheckException("系统错误异常");
        }

        return workOrder.getId().toString();
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        try {
            EasyExcelUtil.downloadLocal(response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req) {
        if(StringUtils.isNotBlank(req.getType()) &&
           !StringUtils.equalsAny(req.getType(), Constants.WORK_ORDER_NEW, Constants.WORK_ORDER_ASK, Constants.WORK_ORDER_QUOTE,
                                  Constants.WORK_ORDER_REVIEW, Constants.WORK_ORDER_ORDER)) {
            throw new CheckException("工单类型不合法");
        }
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.lambdaQuery(WorkOrder.class);
        wrapper.eq(Objects.nonNull(req.getProjectId()), WorkOrder::getProjectId, req.getProjectId())
                .and(StringUtils.isNotBlank(req.getName()), workOrderLambdaQueryWrapper -> workOrderLambdaQueryWrapper
                        .like(WorkOrder::getName, "%" + req.getName() + "%")
                        .or()
                        .eq(WorkOrder::getOrderSn, req.getName()))
                .eq(StringUtils.isNotBlank(req.getStatus()), WorkOrder::getStatus, req.getStatus());
        if(StringUtils.isNotBlank(req.getType())) {
            wrapper.eq(WorkOrder::getType, req.getType());
        }
        if(Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime())) {
            wrapper.between(WorkOrder::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }
        Page<WorkOrder> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<WorkOrderResp> respList = page.getRecords().stream()
                .map(WorkOrderConverter::entity2WorkOrderResp)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public Long create(WorkOrder workOrder) {
        if(Objects.isNull(workOrder.getProjectId())) {
            throw new CheckException("项目ID不能为空");
        }
        Integer count = baseMapper.getCount(workOrder.getProjectId());
        count = count + 1;
        workOrder.setName("第" + count + "批次");
        workOrder.setOrderSn(DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_MS_PATTERN));
        workOrder.setType(Constants.WORK_ORDER_DEMAND);
        workOrder.setStatus(Constants.WORK_ORDER_NEW);
        workOrder.setCtime(DateUtil.date());
        workOrder.setUtime(DateUtil.date());
        workOrder.setCreateUserId(UserInfoContext.getUserId());
        workOrder.setUpdateUserId(UserInfoContext.getUserId());
        //
        save(workOrder);
        return workOrder.getId();
    }

    public List<String> excelTitle() {
        return Arrays.asList("序号", "媒体", "账号", "账号ID或链接", "账号类型", "粉丝数(个)", "资源位置", "数量", "发布开始时间",
                             "发布结束时间", "含电商链接单价", "@", "话题", "电商肖像授权", "品牌双微转发授权", "微任务（针对微博）",
                             "其他特殊说明", "产品提供方", "发布内容brief概述");
    }
}
