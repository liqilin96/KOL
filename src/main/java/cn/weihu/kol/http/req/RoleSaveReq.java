package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "角色保存请求实体类", description = "描述")
public class RoleSaveReq {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "关联权限")
    private List<String> permissionIds;
}
