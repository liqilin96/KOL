package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "工单数据审核请求实体类", description = "描述")
public class WorkOrderDataReviewReq {

    @ApiModelProperty(value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "工单数据ID集合")
    private List<Long> workOrderDataIds;

    @ApiModelProperty(value = "审核结果状态")
    private String status;
}
