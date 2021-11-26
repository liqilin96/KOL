package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新工单数据请求实体类", description = "描述")
public class WorkOrderDataUpdateReq {

    @ApiModelProperty(value = "工单数据ID")
    private Long id;

    @ApiModelProperty(value = "字段组ID")
    private Long fieldsId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "工单数据详情")
    private String data;

    @ApiModelProperty(value = "工单数据状态")
    private String status;

    @ApiModelProperty(value = "1:询价,2:询档")
    private Integer askType;

    @ApiModelProperty(value = "是否库内,0:否,1:是")
    private Integer inbound;
}
