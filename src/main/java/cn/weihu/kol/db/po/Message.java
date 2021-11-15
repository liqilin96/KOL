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
 * 消息记录表
 * </p>
 *
 * @author Lql
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_message")
@ApiModel(value = "Message对象", description = "消息记录表")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "消息类型：1.保价到期提醒")
    private String type;

    @ApiModelProperty(value = "关联用户")
    private Long toUserId;

    @ApiModelProperty(value = "是否已读,1是0否")
    private Integer isReceived;

    @ApiModelProperty(value = "插入时间")
    private LocalDateTime ctime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime utime;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "更新人id")
    private String updateUserId;


}
