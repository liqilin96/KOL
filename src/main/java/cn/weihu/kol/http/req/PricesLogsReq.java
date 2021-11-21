package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/11 16:08
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "报价记录请求实体类", description = "描述")
public class PricesLogsReq {

    @ApiModelProperty(value = "达人名称")
    private String starName;

    @ApiModelProperty(value = "媒体平台")
    private String platform;

    @ApiModelProperty(value = "账号类型")
    private String accountType;

    @ApiModelProperty(value = "报价形式")
    private String pricesForm;

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;

    @ApiModelProperty("达人id，不传则全部,逗号分割")
    private String ids;
}
