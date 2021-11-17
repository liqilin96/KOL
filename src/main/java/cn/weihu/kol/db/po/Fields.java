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

/**
 * <p>
 * 字段组表
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_fields")
@ApiModel(value = "Fields对象", description = "字段组表")
public class Fields implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字段组名称")
    private String name;

    @ApiModelProperty(value = "字段列表")
    private String fieldList;

    /**
     * 类型,1:kol,2:需求,3:报价
     */
    private Integer type;

    @ApiModelProperty(value = "插入时间")
    private LocalDateTime ctime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime utime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;


}
