package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户保存请求实体类", description = "描述")
public class UserSaveReq {

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
    @ApiModelProperty(value = " 密码")
    private String password;

    /**
     * 角色ID集
     */
    @ApiModelProperty(value = "角色")
    private String[] roleIds;

    @ApiModelProperty(value = "合同到期时间")
    private Long contractTime;

}
