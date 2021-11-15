package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工单表记录表
 * </p>
 *
 * @author ${author}
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_work_order_log")
public class WorkOrderLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 更新后数据
     */
    private String data;

    /**
     * 更新前数据
     */
    private String oldData;

    /**
     * 关联工单
     */
    private Long workOrderId;

    /**
     * 工单类型
     */
    private Long type;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 父工单id
     */
    private Long parentId;

    /**
     * 当前状态
     */
    private String status;

    /**
     * 指向处理人
     */
    private Long toUser;

    /**
     * 插入时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date utime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 更新人id
     */
    private String updateUserId;


}
