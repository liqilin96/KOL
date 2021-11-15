package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工单表
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_work_order")
public class WorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderSn;

    private String name;

    @ApiModelProperty(value = "工单类型")
    private Integer type;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    private String projectName;

    @ApiModelProperty(value = "父工单id")
    private Long parentId;

    @ApiModelProperty(value = "当前状态")
    private String status;

    @ApiModelProperty(value = "指向处理人")
    private Long toUser;

    @ApiModelProperty(value = "插入时间")
    private Date ctime;

    @ApiModelProperty(value = "更新时间")
    private Date utime;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "更新人id")
    private String updateUserId;


}
