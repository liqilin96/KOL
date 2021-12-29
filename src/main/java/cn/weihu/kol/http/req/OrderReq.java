package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/12/24 18:06
 * Description： 已下单数据请求
 */
@Setter
@Getter
public class OrderReq {

    @ApiModelProperty(value = "取消合作的ids，英文逗号分割")
    private String workOrderIds;

    @ApiModelProperty(value = "工单id")
    private String workOrderDataId;

    @ApiModelProperty(value = "违约金or制作费")
    private String price;
}
