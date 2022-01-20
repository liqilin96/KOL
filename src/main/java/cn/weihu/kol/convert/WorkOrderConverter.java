package cn.weihu.kol.convert;

import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.util.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class WorkOrderConverter {

    public static WorkOrderResp entity2WorkOrderResp(WorkOrder workOrder) {
        WorkOrderResp resp = new WorkOrderResp();
        resp.setWorkOrderId(workOrder.getId());
        resp.setWorkOrderSn(workOrder.getOrderSn());
        resp.setWorkOrderName(workOrder.getName());
        resp.setType(workOrder.getType());
        resp.setProjectId(workOrder.getProjectId());
        resp.setProjectName(workOrder.getProjectName());
        resp.setStatus(workOrder.getStatus());
        resp.setParentId(workOrder.getParentId());
        resp.setRejectReason(workOrder.getRejectReason());
        resp.setCtime(workOrder.getCtime());
        resp.setUtime(workOrder.getUtime());
        resp.setPdfPath(workOrder.getPdfPath());
        return resp;
    }

    public static WorkOrderDataResp entity2WorkOrderDataResp(WorkOrderData workOrderData) {
        WorkOrderDataResp resp = new WorkOrderDataResp();
        resp.setWorkOrderDataId(workOrderData.getId());
        resp.setFieldsId(workOrderData.getFieldsId());
        resp.setWorkOrderId(workOrderData.getWorkOrderId());
        resp.setStatus(workOrderData.getStatus());
        Map<String, String> map = GsonUtils.gson.fromJson(workOrderData.getData(), new TypeToken<Map<String, String>>() {
        }.getType());
        if(map.get("commission") != null && map.get("commission").endsWith("%")) {
            map.put("commission", map.get("commission").substring(0, map.get("commission").indexOf("%")));
        }
        if(map.get("sale") != null && map.get("sale").endsWith("%")) {
            map.put("sale", map.get("sale").substring(0, map.get("sale").indexOf("%")));
        }
        resp.setData(GsonUtils.gson.toJson(map));
        resp.setCtime(workOrderData.getCtime());
        resp.setUtime(workOrderData.getUtime());
        return resp;
    }

}
