package cn.weihu.kol.convert;

import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderResp;

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

}
