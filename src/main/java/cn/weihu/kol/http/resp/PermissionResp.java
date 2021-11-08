package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "权限响应实体类", description = "权限信息")
public class PermissionResp {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限链接地址")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否有效：0：正常，1无效")
    private Integer status;

    @ApiModelProperty(value = "权限类型：1：目录，2菜单，3按钮")
    private Integer type;

    @ApiModelProperty(value = "上级权限id")
    private String parentId;

    @ApiModelProperty(value = "下级节点")
    private List<PermissionResp> children = new ArrayList<>();

}
