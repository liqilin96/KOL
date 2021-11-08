package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "登录响应实体类", description = "描述")
public class LoginResp {

    @ApiModelProperty(value = "所属企业ID")
    private String companyId;

    @ApiModelProperty(value = "auth")
    private String auth;

    @ApiModelProperty(value = "msgKey")
    private String msgKey;

    @ApiModelProperty(value = "permissions")
    private List<PermissionResp> permissions;

    @ApiModelProperty(value = "是否脱敏")
    private Integer isDesensitization;

    public LoginResp() {
    }

    public LoginResp(String companyId, String auth, String msgKey, List<PermissionResp> permissions,Integer isDesensitization) {
        this.companyId = companyId;
        this.auth = auth;
        this.msgKey = msgKey;
        this.permissions = permissions;
        this.isDesensitization = isDesensitization;
    }
}
