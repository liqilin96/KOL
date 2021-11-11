package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lql
 * @date 2021/11/11 21:21
 * Description：
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PricesLogsBoResp {

    private List<PricesLogsResp> resps;

    private String fieldList;
}
