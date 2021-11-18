package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 报价记录表
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_prices_logs")
@ApiModel(value = "PricesLogs对象", description = "报价记录表")
public class PricesLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "艺人编号")
    private String actorSn;

    @ApiModelProperty(value = "达人信息")
    private String actorData;

    @ApiModelProperty(value = "是否库内")
    private Integer inbound;

    @ApiModelProperty(value = "佣金%")
    private Integer commission;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "供应商")
    private String provider;

    @ApiModelProperty(value = "保价到期时间")
    private Date insureEndtime;

    @ApiModelProperty(value = "插入时间")
    private Date ctime;

    @ApiModelProperty(value = "更新时间")
    private Date utime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;


}
