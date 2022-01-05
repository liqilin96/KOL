package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lql
 * @date 2021/11/10 17:35
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "工单请求实体类", description = "描述")
public class WorkOrderReq {

    @ApiModelProperty(value = "模板类型，1抖音快手，其他非抖音快手")
    private String excelType;

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "工单编号/名称")
    private String name;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "开始时间,13位毫秒级时间戳")
    private Long startTime;

    @ApiModelProperty(value = "结束时间,13位毫秒级时间戳")
    private Long endTime;

    @ApiModelProperty(value = "指向用户ID")
    private Long toUser;

    @ApiModelProperty(value = "工单数据id,多个逗号分割")
    private String workOrderDataIds;
}
