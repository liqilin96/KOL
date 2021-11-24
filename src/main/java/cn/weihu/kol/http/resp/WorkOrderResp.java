package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "工单信息响应实体类", description = "工单信息")
public class WorkOrderResp {

    @ApiModelProperty(value = "需求工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "工单编号")
    private String workOrderSn;

    @ApiModelProperty(value = "工单名称")
    private String workOrderName;

    @ApiModelProperty(value = "工单类型")
    private Integer type;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "父工单ID")
    private Long parentId;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "创建时间")
    private Date ctime;

    @ApiModelProperty(value = "更新时间")
    private Date utime;
}
