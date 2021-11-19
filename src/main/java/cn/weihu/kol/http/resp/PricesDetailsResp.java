package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author lql
 * @date 2021/11/19 14:56
 * Description：
 */
@Setter
@Getter
@AllArgsConstructor
@ApiModel(value = "达人在报数据返回实体类", description = "描述")
public class PricesDetailsResp {

    @ApiModelProperty(value = "保价内的数量")
    private Long total;

    @ApiModelProperty(value = "保价内的数据详情")
    private Map<String, String> map;
}
