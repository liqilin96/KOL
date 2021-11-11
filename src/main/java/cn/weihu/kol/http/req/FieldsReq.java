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

    @ApiModelProperty(value = "字段组名")
    private String name;

    @ApiModelProperty(value = "字段列表")
    private String fieldList;

    @ApiModelProperty(value = "字段类型1账号类型，2报价形式")
    private String type;
}
