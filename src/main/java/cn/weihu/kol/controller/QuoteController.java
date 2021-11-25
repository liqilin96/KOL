package cn.weihu.kol.controller;


import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.QuoteBiz;
import cn.weihu.kol.http.req.QuoteReq;
import cn.weihu.kol.http.resp.QuoteResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "查询", httpMethod = "GET", notes = "报价库-查询")
    @GetMapping(value = "/page")
    public ResultBean<PageResult<QuoteResp>> page(QuoteReq req) {
        return new ResultBean<>(quoteBiz.page(req));
    }
}

