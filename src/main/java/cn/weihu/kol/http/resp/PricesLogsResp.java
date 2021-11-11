package cn.weihu.kol.http.resp;

import cn.weihu.kol.db.po.PricesLogs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/11 17:16
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "报价历史返回实体类", description = "描述")
public class PricesLogsResp extends PricesLogs {



}
