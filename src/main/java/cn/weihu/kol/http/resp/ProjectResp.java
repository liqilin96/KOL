package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author lql
 * @date 2021/11/10 20:12
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "项目信息响应实体类", description = "项目信息")
public class ProjectResp {

    private Long id;

    @ApiModelProperty(value = "项目名")
    private String name;

    @ApiModelProperty(value = "项目描述")
    private String descs;

    @ApiModelProperty(value = "预算")
    private Double budget;

    @ApiModelProperty(value = "立项单")
    private String projectImg;

    private Date ctime;
}
