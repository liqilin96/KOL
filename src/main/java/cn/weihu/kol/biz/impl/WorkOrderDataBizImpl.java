package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.convert.WorkOrderConverter;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.dao.WorkOrderDataDao;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.req.WorkOrderBatchUpdateReq;
import cn.weihu.kol.http.req.WorkOrderDataReq;
import cn.weihu.kol.http.req.WorkOrderDataReviewReq;
import cn.weihu.kol.http.req.WorkOrderDataUpdateReq;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单数据表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class WorkOrderDataBizImpl extends ServiceImpl<WorkOrderDataDao, WorkOrderData> implements WorkOrderDataBiz {

    @Autowired
    private PricesBiz    pricesBiz;
    @Resource
    private WorkOrderDao workOrderDao;

    @Override
    public List<WorkOrderDataResp> workOrderDataList(WorkOrderDataReq req) {
        LambdaQueryWrapper<WorkOrderData> wrapper = Wrappers.lambdaQuery(WorkOrderData.class);
        wrapper.eq(WorkOrderData::getWorkOrderId, req.getWorkOrderId());
        List<WorkOrderData> list = list(wrapper);
        return list.stream().map(WorkOrderConverter::entity2WorkOrderDataResp).collect(Collectors.toList());
    }

    @Override
    public Long updateWorkOrderData(WorkOrderBatchUpdateReq req) {
        if(CollectionUtils.isEmpty(req.getList())) {
            throw new CheckException("更新内容不能为空");
        }
        return null;
    }

    @Override
    public WorkOrderDataScreeningResp screening(WorkOrderBatchUpdateReq req) {
        if(CollectionUtils.isEmpty(req.getList())) {
            throw new CheckException("筛选内容不能为空");
        }
        // 获取所有达人信息
        List<Prices>            pricesList = pricesBiz.list();
        List<WorkOrderDataResp> list       = new ArrayList<>();
        WorkOrderDataResp       workOrderDataResp;
        for(WorkOrderDataUpdateReq updateReq : req.getList()) {
            // 根据 媒体、账号、资源位置 匹配相同需求数据
            // 比对 含电商连接单价、@、话题、电商肖像权、品牌双微转发授权、微任务 是否为库内数据
            List<Prices> collect = pricesList.stream()
                    .filter(prices -> 0 != prices.getInbound())
//                    .filter()
                    .collect(Collectors.toList());
            workOrderDataResp = new WorkOrderDataResp();
            if(collect.isEmpty()) {
                // 非库内数据
                workOrderDataResp.setInbound(0);
            } else {
                // 库内数据
                workOrderDataResp.setInbound(1);
            }
            list.add(workOrderDataResp);
        }
        WorkOrderDataScreeningResp resp = new WorkOrderDataScreeningResp();
        resp.setList(list);
        return resp;
    }

    @Override
    public Long enquiry(WorkOrderBatchUpdateReq req) {
        if(CollectionUtils.isEmpty(req.getList())) {
            throw new CheckException("询价&询档内容不能为空");
        }
        WorkOrder workOrder = workOrderDao.selectById(req.getWorkOrderId());
        if(Objects.isNull(workOrder)) {
            throw new CheckException("需求工单不存在");
        }
        // 更新需求工单状态
        workOrder.setStatus(Constants.WORK_ORDER_ASK);
        workOrder.setUpdateUserId(UserInfoContext.getUserId());
        workOrder.setUtime(DateUtil.date());
        workOrderDao.updateById(workOrder);
        // 生成 询价/询档工单
        WorkOrder askWorkOrder = new WorkOrder();
        askWorkOrder.setOrderSn(workOrder.getOrderSn());
        askWorkOrder.setName(workOrder.getName());
        askWorkOrder.setType(Constants.WORK_ORDER_ENQUIRY);
        askWorkOrder.setProjectId(workOrder.getProjectId());
        askWorkOrder.setProjectName(workOrder.getProjectName());
        askWorkOrder.setParentId(workOrder.getId());
        askWorkOrder.setStatus(Constants.WORK_ORDER_ASK);
        askWorkOrder.setCtime(DateUtil.date());
        askWorkOrder.setUtime(DateUtil.date());
        askWorkOrder.setCreateUserId(UserInfoContext.getUserId());
        askWorkOrder.setUpdateUserId(UserInfoContext.getUserId());
        workOrderDao.insert(workOrder);
        // 更新工单数据
        List<WorkOrderData> workOrderDataList = new ArrayList<>();
        WorkOrderData       workOrderData;
        for(WorkOrderDataUpdateReq workOrderDataUpdateReq : req.getList()) {
            workOrderData = new WorkOrderData();
            workOrderData.setId(workOrderDataUpdateReq.getId());
            if(1 == workOrderDataUpdateReq.getAskType()) {
                // 询价
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_PRICE);
            } else {
                // 询档
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_DATE);
                workOrderData.setData(workOrderData.getData());
            }
            workOrderData.setUtime(DateUtil.date());
            workOrderData.setUpdateUserId(UserInfoContext.getUserId());
            workOrderDataList.add(workOrderData);
        }
        updateBatchById(workOrderDataList);
        return null;
    }

    @Override
    public Long quote(WorkOrderBatchUpdateReq req) {
        if(CollectionUtils.isEmpty(req.getList())) {
            throw new CheckException("报价工单数据不能为空");
        }
        // 更新工单数据状态
        List<WorkOrderData> workOrderDataList = new ArrayList<>();
        WorkOrderData       workOrderData;
        for(WorkOrderDataUpdateReq workOrderDataUpdateReq : req.getList()) {
            workOrderData = new WorkOrderData();
            workOrderData.setId(workOrderDataUpdateReq.getId());
            workOrderData.setStatus(Constants.WORK_ORDER_DATA_REVIEW);
            workOrderData.setData(workOrderDataUpdateReq.getData());
            workOrderData.setUtime(DateUtil.date());
            workOrderData.setUpdateUserId(UserInfoContext.getUserId());
            workOrderDataList.add(workOrderData);
        }
        updateBatchById(workOrderDataList);
        // 插入报价记录表

        // 更新工单状态
        LambdaQueryWrapper<WorkOrderData> wrapper = Wrappers.lambdaQuery(WorkOrderData.class);
        wrapper.eq(WorkOrderData::getWorkOrderId, req.getWorkOrderId())
                .in(WorkOrderData::getStatus, Constants.WORK_ORDER_DATA_ASK_PRICE, Constants.WORK_ORDER_DATA_ASK_DATE);
        List<WorkOrderData> list = list(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            // 更新工单状态为 审核
            WorkOrder workOrder = new WorkOrder();
            workOrder.setId(req.getWorkOrderId());
            workOrder.setStatus(Constants.WORK_ORDER_REVIEW);
            workOrder.setUpdateUserId(UserInfoContext.getUserId());
            workOrder.setUtime(DateUtil.date());
            workOrderDao.updateById(workOrder);
        }
        return null;
    }

    @Override
    public Long review(WorkOrderDataReviewReq req) {
        if(CollectionUtils.isEmpty(req.getWorkOrderDataIds())) {
            throw new CheckException("审核工单数据不能为空");
        }
        if(!StringUtils.equalsAny(req.getStatus(), Constants.WORK_ORDER_DATA_REVIEW_PASS,
                                  Constants.WORK_ORDER_DATA_REVIEW_REJECT)) {
            throw new CheckException("审核状态不合法");
        }
        // 更新工单数据状态
        List<WorkOrderData> workOrderDataList = new ArrayList<>();
        WorkOrderData       workOrderData;
        for(Long workOrderDataId : req.getWorkOrderDataIds()) {
            workOrderData = new WorkOrderData();
            workOrderData.setId(workOrderDataId);
            workOrderData.setStatus(req.getStatus());
            workOrderData.setUtime(DateUtil.date());
            workOrderData.setUpdateUserId(UserInfoContext.getUserId());
            workOrderDataList.add(workOrderData);
        }
        updateBatchById(workOrderDataList);
        //
        return null;
    }
}
