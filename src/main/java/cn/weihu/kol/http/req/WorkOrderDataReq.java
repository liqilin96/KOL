package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工单数据请求实体类", description = "描述")
public class WorkOrderDataReq {

    @ApiModelProperty(value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "账号或媒体名称")
    private String name;

    @ApiModelProperty(value = "供应商:新意/维格")
    private String supplier;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "模板类型，1为抖音快手，其他为非抖音快手")
    private String templateType;

}


