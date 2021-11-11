package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/10 17:35
 * Description：
 */
@Setter
@Getter
@ApiModel(value = "工单请求实体类", description = "描述")
public class WorkOrderReq {

private String name;


}
