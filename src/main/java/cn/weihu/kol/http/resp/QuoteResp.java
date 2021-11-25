package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "报价库信息响应实体类", description = "报价库信息")
public class QuoteResp {

    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 达人编号
     */
    @ApiModelProperty(value = "达人编号")
    private String actorSn;

    /**
     * 达人信息
     */
    @ApiModelProperty(value = "达人信息")
    private String actorData;

    /**
     * 佣金%
     */
    @ApiModelProperty(value = "佣金")
    private Integer commission;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String provider;

    /**
     * 保价到期时间
     */
    @ApiModelProperty(value = "保价到期时间")
    private Date insureEndtime;

    /**
     * 插入时间
     */
    @ApiModelProperty(value = "插入时间")
    private Date ctime;
}
