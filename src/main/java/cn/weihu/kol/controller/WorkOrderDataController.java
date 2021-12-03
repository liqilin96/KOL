package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.http.req.WorkOrderBatchUpdateReq;
import cn.weihu.kol.http.req.WorkOrderDataOrderReq;
import cn.weihu.kol.http.req.WorkOrderDataReq;
import cn.weihu.kol.http.req.WorkOrderDataReviewReq;
import cn.weihu.kol.http.resp.WorkOrderDataCompareResp;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 工单数据表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@RestController
@RequestMapping(value = "workorder/data")
@Api(value = "工单数据管理", tags = "工单数据管理")
public class WorkOrderDataController {

    @Autowired
    private WorkOrderDataBiz dataBiz;

    @ApiOperation(value = "数据列表", httpMethod = "GET", notes = "数据列表")
    @GetMapping(value = "/list")
    public ResultBean<List<WorkOrderDataResp>> workOrderDataList(WorkOrderDataReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "工单ID不能为空");
        return new ResultBean<>(dataBiz.workOrderDataList(req));
    }
    @ApiOperation(value = "已下单导出", httpMethod = "GET", notes = "已报价-报完价待确认导出")
    @GetMapping(value = "/list/pass/export")
    public void workOrderDataListExport(WorkOrderDataReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        dataBiz.workOrderDataListExport(req, response);
    }

    @ApiOperation(value = "待报价数据列表", httpMethod = "GET", notes = "数据列表")
    @GetMapping(value = "/wait/list")
    public ResultBean<List<WorkOrderDataResp>> waitWorkOrderDataList(WorkOrderDataReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        return new ResultBean<>(dataBiz.waitWorkOrderDataList(req));
    }

    @ApiOperation(value = "批量更新", httpMethod = "PUT", notes = "批量更新")
    @PutMapping(value = "/update/batch")
    public ResultBean<Long> updateWorkOrderData(@RequestBody WorkOrderBatchUpdateReq req) {
        return new ResultBean<>(dataBiz.updateWorkOrderData(req));
    }

    @ApiOperation(value = "库内外筛选", httpMethod = "POST", notes = "库内外筛选")
    @PostMapping(value = "/screening")
    public ResultBean<WorkOrderDataScreeningResp> screening(@RequestBody WorkOrderBatchUpdateReq req) {
        CheckUtil.notNull(req.getProjectId(), "项目ID不能为空");
        return new ResultBean<>(dataBiz.screening(req));
    }

    @ApiOperation(value = "询价&询档", httpMethod = "POST", notes = "询价&询档")
    @PostMapping(value = "/enquiry")
    public ResultBean<Long> enquiry(@RequestBody WorkOrderBatchUpdateReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        return new ResultBean<>(dataBiz.enquiry(req));
    }

    @ApiOperation(value = "重新询价", httpMethod = "POST", notes = "重新询价")
    @PostMapping(value = "/enquiry/again")
    public ResultBean<Long> enquiryAgain(@RequestBody WorkOrderBatchUpdateReq req) {
        CheckUtil.notNull(req.getRequestType(), "请求类型不能为空");
        return new ResultBean<>(dataBiz.enquiryAgain(req));
    }

    @ApiOperation(value = "报价", httpMethod = "POST", notes = "报价")
    @PostMapping(value = "/quote")
    public ResultBean<Long> quote(@RequestBody WorkOrderBatchUpdateReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        return new ResultBean<>(dataBiz.quote(req));
    }

    @ApiOperation(value = "报价待确认列表", httpMethod = "GET", notes = "报价待确认列表")
    @GetMapping(value = "/quote/list")
    public ResultBean<WorkOrderDataCompareResp> quoteList(WorkOrderDataReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        return new ResultBean<>(dataBiz.quoteList(req));
    }

    @ApiOperation(value = "已报价-报完价待确认导出", httpMethod = "GET", notes = "已报价-报完价待确认导出")
    @GetMapping(value = "/quote/list/export")
    public void quoteListExport(WorkOrderBatchUpdateReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        dataBiz.quoteListExport(req, response);
    }

    @ApiOperation(value = "提审", httpMethod = "POST", notes = "提审")
    @PostMapping(value = "/order")
    public ResultBean<Long> order(@RequestBody WorkOrderDataOrderReq req) {
        CheckUtil.notNull(req.getProjectId(), "项目ID不能为空");
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        return new ResultBean<>(dataBiz.order(req));
    }

    @ApiOperation(value = "审核", httpMethod = "POST", notes = "审核")
    @PostMapping(value = "/review")
    public ResultBean<Long> review(@RequestBody WorkOrderDataReviewReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        CheckUtil.notEmpty(req.getStatus(), "审核结果状态不能为空");
        return new ResultBean<>(dataBiz.review(req));
    }

    @ApiOperation(value = "初次创建需求单详情导出", httpMethod = "GET", notes = "初次创建需求单详情导出")
    @GetMapping(value = "/detail/export")
    public void expirtPricesExport(WorkOrderBatchUpdateReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        dataBiz.detailExport(req, response);
    }


    @ApiOperation(value = "供应商报价导出", httpMethod = "GET", notes = "供应商报价导出")
    @GetMapping(value = "/supplier/export")
    public void supplierExport(WorkOrderBatchUpdateReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkerOrderDataIds(), "需求工单ID不能为空");
        dataBiz.supplierExport(req, response);
    }

}

