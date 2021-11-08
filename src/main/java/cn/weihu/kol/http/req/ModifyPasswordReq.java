package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "修改密码请求实体类", description = "描述")
public class ModifyPasswordReq {

    @ApiModelProperty(value = "历史密码")
    private String oldPassword;

    @ApiModelProperty(value = "密码")
    private String password;
}
