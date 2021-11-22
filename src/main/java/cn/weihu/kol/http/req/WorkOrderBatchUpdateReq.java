package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "批量更新工单数据请求实体类", description = "描述")
public class WorkOrderBatchUpdateReq {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "需求工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "更新工单数据列表")
    private List<WorkOrderDataUpdateReq> list;

    @ApiModelProperty("工单id，逗号分割")
    private String workerOrderIds;
}
