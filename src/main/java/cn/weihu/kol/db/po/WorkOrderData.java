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
 * 工单数据表
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_work_order_data")
public class WorkOrderData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字段组id")
    private Long fieldsId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "工单id")
    private Long workOrderId;

    @ApiModelProperty(value = "当前状态")
    private String status;

    @ApiModelProperty(value = "数据详情json")
    private String data;

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
