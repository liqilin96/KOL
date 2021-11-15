package cn.weihu.kol.controller;


import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesLogsBoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 达人报价表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/prices")
@Api(value = "达人报价记录管理", tags = "报价记录管理")
public class PricesController {

    @Autowired
    private PricesBiz pricesBiz;

    @ApiOperation(value = "达人报价", httpMethod = "GET", notes = "达人报价")
    @GetMapping(value = "/star")
    public ResultBean<PricesLogsBoResp> starPrice(@RequestBody PricesLogsReq req) {
        return new ResultBean<>(pricesBiz.starPrice(req));
    }




//    @ApiOperation(value = "报价记录", httpMethod = "GET", notes = "报价记录")
//    @GetMapping(value = "/query/page")
//    public ResultBean<PageResult<PricesLogsBoResp>> page(@RequestBody PricesLogsReq req) {
//        return new ResultBean<>(pricesBiz.pages(req));
//    }
}

