package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "登录响应实体类", description = "描述")
public class LoginResp {


    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色")
    private List<String> roleIds;

    @ApiModelProperty(value = "auth")
    private String auth;

    @ApiModelProperty(value = "msgKey")
    private String msgKey;

    @ApiModelProperty(value = "permissions")
    private List<PermissionResp> permissions;


}
