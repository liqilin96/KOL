package cn.weihu.kol.controller;


import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.PricesLogsBiz;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.req.ProjectReq;
import cn.weihu.kol.http.resp.PricesLogsResp;
import cn.weihu.kol.http.resp.ProjectResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 报价记录表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/prices/logs")
@Api(value = "报价记录管理", tags = "报价记录管理")
public class PricesLogsController {


    @Autowired
    private PricesLogsBiz pricesLogsBiz;

//    @ApiOperation(value = "查询", httpMethod = "GET", notes = "查询")
//    @GetMapping(value = "/query/page")
//    public ResultBean<PageResult<PricesLogsResp>> page(@RequestBody PricesLogsReq req) {
//        return new ResultBean<>(pricesLogsBiz.pages(req));
//    }
}

