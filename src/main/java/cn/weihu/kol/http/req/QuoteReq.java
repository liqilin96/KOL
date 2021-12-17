package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报价库信息请求实体类", description = "报价库信息")
public class QuoteReq {

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;

//    @ApiModelProperty(value = "达人id")
//    private String starId;

    @ApiModelProperty(value = "达人名")
    private String starName;

    @ApiModelProperty(value = "达人编号")
    private String actorSn;

    @ApiModelProperty(value = "媒体平台")
    private String platform;

//    @ApiModelProperty(value = "账号类型")
//    private String accountType;

    @ApiModelProperty(value = "报价形式")
    private String pricesForm;

//    @ApiModelProperty("达人id，不传则全部,逗号分割")
//    private String ids;
//
//    @ApiModelProperty("排序，DESC降序，ASC升序，不区分大小写")
//    private String orderBy;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;
}
