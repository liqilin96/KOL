package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lql
 * @date 2021/11/19 15:22
 * Description：
 */
@Data
@ApiModel(value = "达人报价详情导出请求实体类", description = "描述")
public class StarExportDataReq {


    @ApiModelProperty("达人数据，不传则全部")
    private String ids;


}
