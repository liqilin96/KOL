package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2022/1/19 13:18
 * Description： 媒介请求实体
 */
@Getter
@Setter
public class MediumReq {

    @ApiModelProperty(value = "品牌方id")
    private String brandId;

    @ApiModelProperty(value = "媒介id")
    private String mediumId;

}
