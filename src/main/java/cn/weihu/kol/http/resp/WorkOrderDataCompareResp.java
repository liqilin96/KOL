package cn.weihu.kol.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "工单数据比对响应实体类", description = "工单数据信息")
public class WorkOrderDataCompareResp {

    @ApiModelProperty(value = "库外数据")
    private Map<String, List<WorkOrderDataResp>> outboundMap;

    @ApiModelProperty(value = "新意")
    private List<WorkOrderDataResp> xinYiList;

    @ApiModelProperty(value = "维格")
    private List<WorkOrderDataResp> weiGeList;
}
