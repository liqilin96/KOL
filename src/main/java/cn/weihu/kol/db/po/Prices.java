package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 达人报价表
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_prices")
@ApiModel(value = "Prices对象", description = "达人报价表")
public class Prices implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "达人编号")
    private String actorSn;

    @ApiModelProperty(value = "达人信息")
    private String actorData;

    @ApiModelProperty(value = "佣金%")
    private Integer commission;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "供应商")
    private String provider;

    @ApiModelProperty(value = "保价到期时间")
    private Date insureEndtime;

    @ApiModelProperty(value = "是否重新询价，0否1是")
    private Integer isReQuote;

    @ApiModelProperty(value = "插入时间")
    private Date ctime;

    @ApiModelProperty(value = "更新时间")
    private Date utime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;

    @ApiModelProperty(value = "报价仅保留一天，0否1是")
    private String priceOnlyDay;

}
