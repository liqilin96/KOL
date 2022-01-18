package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/10 16:48
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "项目请求实体类", description = "描述")
public class ProjectReq {

    @ApiModelProperty(value = "项目id")
    private String id;

    @ApiModelProperty(value = "项目名")
    private String name;

    @ApiModelProperty(value = "项目说明")
    private String desc;

    @ApiModelProperty(value = "预算")
    private String budget;

    @ApiModelProperty(value = "项目立项单")
    private String projectImg;

//    @ApiModelProperty(value = "项目新名")
//    private String newName;

    @ApiModelProperty(value = "工单配置")
    private WorkOrderReq workOrderReq;

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

}
