package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.req.StarExportDataReq;
import cn.weihu.kol.http.resp.PricesDetailsResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

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

    //    @ApiOperation(value = "达人报价", httpMethod = "GET", notes = "达人报价")
//    @GetMapping(value = "/star")
//    public ResultBean<PricesLogsBoResp> starPrice(PricesLogsReq req) {
//        return new ResultBean<>(pricesBiz.starPrice(req));
//    }
    @ApiOperation(value = "达人在保详情", httpMethod = "GET", notes = "达人信息-分页")
    @GetMapping(value = "/star/detail")
    public ResultBean<PricesDetailsResp> starDetail(PricesLogsReq req) {
        return new ResultBean<>(pricesBiz.starDetail(req));
    }


    @ApiOperation(value = "达人信息-分页", httpMethod = "GET", notes = "达人信息-分页")
    @GetMapping(value = "/star")
    public ResultBean<PageResult<PricesLogsResp>> starPage(PricesLogsReq req) {
        return new ResultBean<>(pricesBiz.starPage(req));
    }


    @ApiOperation(value = "达人报价详情-分页", httpMethod = "GET", notes = "达人报价详情-分页")
    @GetMapping(value = "/star/page")
    public ResultBean<PageResult<PricesLogsResp>> starPricePage(PricesLogsReq req) {
        CheckUtil.notEmpty(req.getStarName(), "达人名称不能为空");
        CheckUtil.notEmpty(req.getPlatform(), "媒体平台不能为空");
        return new ResultBean<>(pricesBiz.starPricePage(req));
    }

    @ApiOperation(value = "达人报价详情导出", httpMethod = "GET", notes = "达人报价详情导出")
    @GetMapping(value = "/star/export")
    public void export(StarExportDataReq req, HttpServletResponse response) {
        pricesBiz.exportStarData(response, req);
    }


    @ApiOperation(value = "查询达人标签", httpMethod = "GET", notes = "达人标签查询返回")
    @GetMapping(value = "/star/{tab}")
    public ResultBean<Set<String>> starTab(@PathVariable("tab") String tab) {
        CheckUtil.notEmpty(tab, "标签不能为空");
        return new ResultBean<>(pricesBiz.starTab(tab));
    }

}

