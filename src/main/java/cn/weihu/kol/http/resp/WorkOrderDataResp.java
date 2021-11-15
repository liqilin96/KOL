package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "工单数据信息响应实体类", description = "工单数据信息")
public class WorkOrderDataResp {

    @ApiModelProperty(value = "工单数据ID")
    private Long workOrderDataId;

    @ApiModelProperty(value = "字段组ID")
    private Long fieldsId;

    @ApiModelProperty(value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(value = "当前状态")
    private String status;

    @ApiModelProperty(value = "数据详情")
    private String data;

    @ApiModelProperty(value = "创建时间")
    private Date ctime;

    @ApiModelProperty(value = "更新时间")
    private Date utime;
}
