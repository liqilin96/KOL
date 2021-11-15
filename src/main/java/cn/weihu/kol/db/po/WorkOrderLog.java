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
 * 工单表记录表
 * </p>
 *
 * @author Lql
 * @since 2021-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_work_order_log")
@ApiModel(value="WorkOrderLog对象", description="工单表记录表")
public class WorkOrderLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "更新后数据")
    private String data;

    @ApiModelProperty(value = "更新前数据")
    private String oldData;

    @ApiModelProperty(value = "关联工单")
    private Long workOrderId;

    @ApiModelProperty(value = "工单类型")
    private Long type;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "父工单id")
    private Long parentId;

    @ApiModelProperty(value = "当前状态")
    private String status;

    @ApiModelProperty(value = "指向处理人")
    private Long toUser;

    @ApiModelProperty(value = "插入时间")
    private LocalDateTime ctime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime utime;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "更新人id")
    private String updateUserId;


}
