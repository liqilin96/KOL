package cn.weihu.kol.convert;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.db.po.PricesLogs;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.req.WorkOrderDataUpdateReq;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class WorkOrderConverter {

    public static WorkOrderResp entity2WorkOrderResp(WorkOrder workOrder) {
        WorkOrderResp resp = new WorkOrderResp();
        resp.setWorkOrderId(workOrder.getId());
        resp.setWorkOrderSn(workOrder.getOrderSn());
        resp.setWorkOrderName(workOrder.getName());
        resp.setProjectId(workOrder.getProjectId());
        resp.setProjectName(workOrder.getProjectName());
        resp.setStatus(workOrder.getStatus());
        resp.setCtime(workOrder.getCtime());
        resp.setUtime(workOrder.getUtime());
        return resp;
    }

    public static WorkOrderDataResp entity2WorkOrderDataResp(WorkOrderData workOrderData) {
        WorkOrderDataResp resp = new WorkOrderDataResp();
        resp.setWorkOrderDataId(workOrderData.getId());
        resp.setFieldsId(workOrderData.getFieldsId());
        resp.setWorkOrderId(workOrderData.getWorkOrderId());
        resp.setStatus(workOrderData.getStatus());
        resp.setData(workOrderData.getData());
        resp.setCtime(workOrderData.getCtime());
        resp.setUtime(workOrderData.getUtime());
        return resp;
    }

    public static PricesLogs workOrderData2PricesLogs(WorkOrderDataUpdateReq workOrderData) {
        PricesLogs pricesLogs = new PricesLogs();
        pricesLogs.setActorSn(UUID.randomUUID().toString());
        pricesLogs.setActorData(workOrderData.getData());
        pricesLogs.setInbound(workOrderData.getInbound());
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = GsonUtils.gson.fromJson(workOrderData.getData(), type);
        pricesLogs.setCommission(StringUtils.isNotBlank(map.get(Constants.ACTOR_COMMISSION)) ?
                                 Integer.parseInt(map.get(Constants.ACTOR_COMMISSION)) : 0);
        pricesLogs.setPrice(StringUtils.isNotBlank(map.get(Constants.ACTOR_PRICE)) ?
                            Double.parseDouble(map.get(Constants.ACTOR_PRICE)) : 0);
        pricesLogs.setProvider(map.get(Constants.ACTOR_PROVIDER));
        pricesLogs.setInsureEndtime(StringUtils.isNotBlank(map.get(Constants.ACTOR_INSURE)) ?
                                    DateUtil.parse(map.get(Constants.ACTOR_INSURE), DatePattern.NORM_DATETIME_PATTERN) : null);
        pricesLogs.setCtime(DateUtil.date());
        pricesLogs.setUtime(DateUtil.date());
        return pricesLogs;
    }
}
