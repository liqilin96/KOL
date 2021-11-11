package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/10 20:04
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "字段配置请求实体类", description = "描述")
public class FieldsReq {

    @ApiModelProperty(value = "字段id")
    private String id;

//    @ApiModelProperty(value = "组id")
//    private String groupId;

    @ApiModelProperty(value = "字段组名")
    private String name;

    @ApiModelProperty(value = "字段列表")
    private String fieldList;

//    @ApiModelProperty(value = "字段类型1账号类型，2报价形式")
//    private String type;

    @ApiModelProperty(value = "要修改的字段组名字")
    private String newName;

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;
}
