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
 * 系统的操作记录表
 * </p>
 *
 * @author Lql
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("base_system_log")
@ApiModel(value = "SystemLog对象", description = "系统的操作记录表")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "操作人")
    private String userName;

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "操作的接口")
    private String operationInterface;

    @ApiModelProperty(value = "入参")
    private String inParams;

    @ApiModelProperty(value = "返回值")
    private String returnValue;


}
