package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.QuoteBiz;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.req.QuoteReq;
import cn.weihu.kol.http.resp.QuoteResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 达人报价表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-11-21
 */
@RestController
@RequestMapping(value = "quote")
@Api(value = "报价库", tags = "报价库")
public class QuoteController {

    @Autowired
    private QuoteBiz quoteBiz;

//    @ApiOperation(value = "查询", httpMethod = "GET", notes = "报价库-查询")
//    @GetMapping(value = "/page")
//    public ResultBean<PageResult<QuoteResp>> page(QuoteReq req) {
//        return new ResultBean<>(quoteBiz.page(req));
//    }


    @ApiOperation(value = "报价库列表-分页", httpMethod = "GET", notes = "报价库列表-分页")
    @GetMapping(value = "/page")
    public ResultBean<PageResult<QuoteResp>> starPage(QuoteReq req) {
        return new ResultBean<>(quoteBiz.quotePage(req));
    }


    @ApiOperation(value = "报价库列表详情-分页", httpMethod = "GET", notes = "报价库列表详情-分页")
    @GetMapping(value = "/price/detail")
    public ResultBean<PageResult<QuoteResp>> priceDetailPage(QuoteReq req) {
        CheckUtil.notEmpty(req.getActorSn(), "达人编号不能为空");
        return new ResultBean<>(quoteBiz.starPricePage(req));
    }

    @ApiOperation(value = "报价库详情导出", httpMethod = "GET", notes = "报价库详情导出")
    @GetMapping(value = "/detail/export")
    public void export(PricesLogsReq req, HttpServletResponse response) {
        quoteBiz.exportQuoteData(response, req);
    }


}

