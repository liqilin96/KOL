package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "用户信息响应实体类", description = "用户信息")
public class UserResp {

    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 用户状态：0正常，1禁用
     */
    @ApiModelProperty(value = "用户状态")
    private Integer status;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private String roleIds;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    private String roleNames;

    /**
     * 插入时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date ctime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date utime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;
}
