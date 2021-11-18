package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.PricesLogsBiz;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.convert.WorkOrderConverter;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.dao.WorkOrderDataDao;
import cn.weihu.kol.db.po.*;
import cn.weihu.kol.http.req.WorkOrderBatchUpdateReq;
import cn.weihu.kol.http.req.WorkOrderDataReq;
import cn.weihu.kol.http.req.WorkOrderDataReviewReq;
import cn.weihu.kol.http.req.WorkOrderDataUpdateReq;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Slf4j
@Service
public class WorkOrderDataBizImpl extends ServiceImpl<WorkOrderDataDao, WorkOrderData> implements WorkOrderDataBiz {


    @Autowired
    private FieldsBiz     fieldsBiz;
    @Autowired
    private PricesBiz     pricesBiz;
    @Autowired
    private PricesLogsBiz pricesLogsBiz;
    @Resource
    private WorkOrderDao  workOrderDao;

    @Override
    public List<WorkOrderDataResp> workOrderDataList(WorkOrderDataReq req) {
        LambdaQueryWrapper<WorkOrderData> wrapper = Wrappers.lambdaQuery(WorkOrderData.class);
        wrapper.eq(WorkOrderData::getWorkOrderId, req.getWorkOrderId())
                .like(StringUtils.isNotBlank(req.getSupplier()), WorkOrderData::getData, req.getSupplier());
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
                    .filter(prices -> screeningBasis(prices.getActorData(), updateReq.getData()))
                    .filter(prices -> screeningOther(prices.getActorData(), updateReq.getData()))
                    .collect(Collectors.toList());
            workOrderDataResp = new WorkOrderDataResp();
            if(collect.isEmpty()) {
                // 非库内数据
                workOrderDataResp.setWorkOrderDataId(updateReq.getId());
                workOrderDataResp.setFieldsId(updateReq.getFieldsId());
                workOrderDataResp.setWorkOrderId(updateReq.getWorkOrderId());
                workOrderDataResp.setStatus(Constants.WORK_ORDER_DATA_NEW);
                workOrderDataResp.setData(updateReq.getData());
                workOrderDataResp.setInbound(0);
            } else {
                // 库内数据
                workOrderDataResp.setWorkOrderDataId(updateReq.getId());
                workOrderDataResp.setFieldsId(updateReq.getFieldsId());
                workOrderDataResp.setWorkOrderId(updateReq.getWorkOrderId());
                workOrderDataResp.setStatus(Constants.WORK_ORDER_DATA_NEW);
                workOrderDataResp.setData(collect.get(0).getActorData());
                workOrderDataResp.setInbound(1);
            }
            list.add(workOrderDataResp);
        }
        WorkOrderDataScreeningResp resp = new WorkOrderDataScreeningResp();
        // 标题
        Fields fields = fieldsBiz.getOneByType(Constants.FIELD_TYPE_DEMAND);
        Type type = new TypeToken<List<FieldsBo>>() {
        }.getType();
        List<FieldsBo> titles = GsonUtils.gson.fromJson(fields.getFieldList(), type);
        resp.setTitles(titles);
        // 数据
        resp.setList(list);
        return resp;
    }

    private boolean screeningBasis(String inbound, String outbound) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        log.info(">>> inbound:{}", inbound);
        log.info(">>> outbound:{}", outbound);
        Map<String, String> inboundMap  = GsonUtils.gson.fromJson(inbound, type);
        Map<String, String> outboundMap = GsonUtils.gson.fromJson(outbound, type);
        if(!inboundMap.get(Constants.TITLE_MEDIA).equals(outboundMap.get(Constants.TITLE_MEDIA))) {
            return false;
        }
        log.info(">>> 媒体匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_MEDIA), outboundMap.get(Constants.TITLE_MEDIA));
        if(!inboundMap.get(Constants.TITLE_ACCOUNT).equals(outboundMap.get(Constants.TITLE_ACCOUNT))) {
            return false;
        }
        log.info(">>> 账号匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_ACCOUNT), outboundMap.get(Constants.TITLE_ACCOUNT));
        if(!inboundMap.get(Constants.TITLE_RESOURCE_LOCATION).equals(outboundMap.get(Constants.TITLE_RESOURCE_LOCATION))) {
            return false;
        }
        log.info(">>> 资源位置匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_RESOURCE_LOCATION), outboundMap.get(Constants.TITLE_RESOURCE_LOCATION));
        return true;
    }

    private boolean screeningOther(String inbound, String outbound) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        log.info(">>> inbound:{}", inbound);
        log.info(">>> outbound:{}", outbound);
        Map<String, String> inboundMap  = GsonUtils.gson.fromJson(inbound, type);
        Map<String, String> outboundMap = GsonUtils.gson.fromJson(outbound, type);
        if("否".equals(inboundMap.get(Constants.TITLE_LINK_PRICE)) && "是".equals(outboundMap.get(Constants.TITLE_LINK_PRICE))) {
            return false;
        }
        log.info(">>> 含电商链接单价匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_LINK_PRICE), outboundMap.get(Constants.TITLE_LINK_PRICE));
        if("否".equals(inboundMap.get(Constants.TITLE_AT)) && "是".equals(outboundMap.get(Constants.TITLE_AT))) {
            return false;
        }
        log.info(">>> AT匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_AT), outboundMap.get(Constants.TITLE_AT));
        if("否".equals(inboundMap.get(Constants.TITLE_TOPIC)) && "是".equals(outboundMap.get(Constants.TITLE_TOPIC))) {
            return false;
        }
        log.info(">>> 话题匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_TOPIC), outboundMap.get(Constants.TITLE_TOPIC));
        if("否".equals(inboundMap.get(Constants.TITLE_STORE_AUTH)) && "是".equals(outboundMap.get(Constants.TITLE_STORE_AUTH))) {
            return false;
        }
        log.info(">>> 电商肖像权匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_STORE_AUTH), outboundMap.get(Constants.TITLE_STORE_AUTH));
        if("否".equals(inboundMap.get(Constants.TITLE_SHARE_AUTH)) && "是".equals(outboundMap.get(Constants.TITLE_SHARE_AUTH))) {
            return false;
        }
        log.info(">>> 品牌双微转发授权匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_SHARE_AUTH), outboundMap.get(Constants.TITLE_SHARE_AUTH));
        if("否".equals(inboundMap.get(Constants.TITLE_MICRO_TASK)) && "是".equals(outboundMap.get(Constants.TITLE_MICRO_TASK))) {
            return false;
        }
        log.info(">>> 微任务匹配成功...inbound:{},outbound:{}",
                 inboundMap.get(Constants.TITLE_MICRO_TASK), outboundMap.get(Constants.TITLE_MICRO_TASK));
        return true;
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
        // 更新工单数据
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        List<WorkOrderData> workOrderDataList    = new ArrayList<>();
        List<WorkOrderData> workOrderDataListNew = new ArrayList<>();
        WorkOrderData       workOrderData;
        WorkOrderData       workOrderDataNew;
        for(WorkOrderDataUpdateReq workOrderDataUpdateReq : req.getList()) {
            workOrderData = new WorkOrderData();
            workOrderData.setId(workOrderDataUpdateReq.getId());
            if(1 == workOrderDataUpdateReq.getAskType()) {
                // 询价
                // 不存在供应商
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_PRICE);
                Map<String, String> map = GsonUtils.gson.fromJson(workOrderDataUpdateReq.getData(), type);
                map.put(Constants.SUPPLIER_FIELD, Constants.SUPPLIER_XINYI);
                workOrderData.setData(GsonUtils.gson.toJson(map));
                // 同步新增一条数据
                workOrderDataNew = new WorkOrderData();
                workOrderDataNew.setFieldsId(workOrderDataUpdateReq.getFieldsId());
                workOrderDataNew.setProjectId(workOrderDataUpdateReq.getProjectId());
                workOrderDataNew.setWorkOrderId(workOrderDataUpdateReq.getWorkOrderId());
                workOrderDataNew.setStatus(Constants.WORK_ORDER_DATA_ASK_PRICE);
                map.put(Constants.SUPPLIER_FIELD, Constants.SUPPLIER_WEIGE);
                workOrderDataNew.setData(GsonUtils.gson.toJson(map));
                workOrderDataNew.setCtime(DateUtil.date());
                workOrderDataNew.setUtime(DateUtil.date());
                workOrderDataListNew.add(workOrderDataNew);
            } else {
                // 询档
                // 存在供应商
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_ASK_DATE);
                workOrderData.setData(workOrderData.getData());
            }
            workOrderData.setUtime(DateUtil.date());
            workOrderData.setUpdateUserId(UserInfoContext.getUserId());
            workOrderDataList.add(workOrderData);
        }
        updateBatchById(workOrderDataList);
        if(!workOrderDataListNew.isEmpty()) {
            saveBatch(workOrderDataListNew);
            // 生成 询价/询档工单
            createPointWorkOrder(workOrder, Constants.SUPPLIER_USER_WEIGE);
        }
        // 生成 询价/询档工单
        createPointWorkOrder(workOrder, Constants.SUPPLIER_USER_XINYI);
        // 更新需求工单状态
        workOrder.setStatus(Constants.WORK_ORDER_ASK);
        workOrder.setUpdateUserId(UserInfoContext.getUserId());
        workOrder.setUtime(DateUtil.date());
        workOrderDao.updateById(workOrder);
        return null;
    }

    private void createPointWorkOrder(WorkOrder workOrder, Long userId) {
        WorkOrder askWorkOrder = new WorkOrder();
        askWorkOrder.setOrderSn(workOrder.getOrderSn());
        askWorkOrder.setName(workOrder.getName());
        askWorkOrder.setType(Constants.WORK_ORDER_ENQUIRY);
        askWorkOrder.setProjectId(workOrder.getProjectId());
        askWorkOrder.setProjectName(workOrder.getProjectName());
        askWorkOrder.setParentId(workOrder.getId());
        askWorkOrder.setStatus(Constants.WORK_ORDER_ASK);
        askWorkOrder.setToUser(userId);
        askWorkOrder.setCtime(DateUtil.date());
        askWorkOrder.setUtime(DateUtil.date());
        askWorkOrder.setCreateUserId(UserInfoContext.getUserId());
        askWorkOrder.setUpdateUserId(UserInfoContext.getUserId());
        workOrderDao.insert(workOrder);
    }

    @Override
    public Long quote(WorkOrderBatchUpdateReq req) {
        if(CollectionUtils.isEmpty(req.getList())) {
            throw new CheckException("报价工单数据不能为空");
        }
        // 更新工单数据状态
        List<WorkOrderData> workOrderDataList = new ArrayList<>();
        List<PricesLogs>    pricesLogsList    = new ArrayList<>();
        WorkOrderData       workOrderData;
        PricesLogs          pricesLogs;
        for(WorkOrderDataUpdateReq workOrderDataUpdateReq : req.getList()) {
            workOrderData = new WorkOrderData();
            workOrderData.setId(workOrderDataUpdateReq.getId());
            workOrderData.setStatus(Constants.WORK_ORDER_QUOTE);
            workOrderData.setData(workOrderDataUpdateReq.getData());
            workOrderData.setUtime(DateUtil.date());
            workOrderData.setUpdateUserId(UserInfoContext.getUserId());
            workOrderDataList.add(workOrderData);

            pricesLogs = WorkOrderConverter.workOrderData2PricesLogs(workOrderDataUpdateReq);
            pricesLogsList.add(pricesLogs);
        }
        updateBatchById(workOrderDataList);
        // 插入报价记录表
        pricesLogsBiz.saveBatch(pricesLogsList);
        // 更新工单状态
        LambdaQueryWrapper<WorkOrderData> wrapper = Wrappers.lambdaQuery(WorkOrderData.class);
        wrapper.eq(WorkOrderData::getWorkOrderId, req.getWorkOrderId())
                .in(WorkOrderData::getStatus, Constants.WORK_ORDER_DATA_ASK_PRICE, Constants.WORK_ORDER_DATA_ASK_DATE);
        List<WorkOrderData> list = list(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            // 更新工单状态为 已报价
            WorkOrder workOrder = new WorkOrder();
            workOrder.setId(req.getWorkOrderId());
            workOrder.setStatus(Constants.WORK_ORDER_QUOTE);
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
        // 下单
        if(Constants.WORK_ORDER_DATA_REVIEW_PASS.equals(req.getStatus())) {
            // 审核通过处理
            List<WorkOrderData> list = list(new LambdaQueryWrapper<>(WorkOrderData.class)
                                                    .in(WorkOrderData::getId, req.getWorkOrderDataIds()));
            if(!CollectionUtils.isEmpty(list)) {
                // 判断库内外 库内更新 库外新增

            }
        } else {
            // TODO: 2021/11/18   审核驳回处理

        }
        return null;
    }
}
