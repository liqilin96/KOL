package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "角色信息响应实体类", description = "角色信息")
public class RoleResp {

    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

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

    /**
     * 角色所属企业
     */
    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "权限id集")
    private List<String> permissionIds;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;
}
