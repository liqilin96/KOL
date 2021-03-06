package cn.weihu.kol.http.resp;

import cn.weihu.kol.biz.bo.FieldsBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "工单数据筛选响应实体类", description = "工单数据信息")
public class WorkOrderDataScreeningResp {

    @ApiModelProperty(value = "标题")
    private List<FieldsBo> titles;

    @ApiModelProperty(value = "工单数据筛选结果列表")
    private List<WorkOrderDataResp> list;
}
