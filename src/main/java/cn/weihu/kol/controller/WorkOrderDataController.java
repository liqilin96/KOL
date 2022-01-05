package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.http.req.*;
import cn.weihu.kol.http.resp.WorkOrderDataCompareResp;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value = "已下单导出", httpMethod = "GET", notes = "已下单导出")
    @GetMapping(value = "/list/pass/export")
    public void workOrderDataListExport(WorkOrderDataReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        CheckUtil.notNull(req.getStatus(), "工单状态不能为空");
        CheckUtil.notNull(req.getTemplateType(), "模板类型不能为空");
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

    @ApiOperation(value = "导入重新询价", httpMethod = "POST", notes = "导入重新询价（报完价待确认）")
    @PostMapping("/enquiry/import")
    public ResultBean<String> enquiryImport(@RequestParam("file") MultipartFile file, WorkOrderReq req, HttpServletResponse response) {
        CheckUtil.notNull(file, "上传的Excel数据文件为空");
        CheckUtil.notEmpty(req.getExcelType(), "Excel类型不能为空");
        CheckUtil.notEmpty(req.getWorkOrderId(), "工单ID不能为空");
        return new ResultBean<>(dataBiz.enquiryImport(file, req, response));
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
        CheckUtil.notNull(req.getTemplateType(), "模板类型不能为空");
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

    @ApiOperation(value = "库内外筛选导出", httpMethod = "GET", notes = "库内外筛选导出")
    @GetMapping(value = "/detail/export")
    public void expirtPricesExport(WorkOrderBatchUpdateReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkOrderId(), "需求工单ID不能为空");
        CheckUtil.notNull(req.getTemplateType(), "模板类型不能为空");
        dataBiz.detailExport(req, response);
    }


    @ApiOperation(value = "供应商报价导出", httpMethod = "GET", notes = "供应商报价导出")
    @GetMapping(value = "/supplier/export")
    public void supplierExport(WorkOrderBatchUpdateReq req, HttpServletResponse response) {
        CheckUtil.notNull(req.getWorkerOrderDataIds(), "需求工单ID不能为空");
        CheckUtil.notNull(req.getTemplateType(), "模板类型不能为空");
        dataBiz.supplierExport(req, response);
    }

    @ApiOperation(value = "供应商报价导入", httpMethod = "POST", notes = "供应商报价导入")
    @PostMapping("/supplier/import")
    public ResultBean<String> supplierImport(@RequestParam("file") MultipartFile file, WorkOrderReq req, HttpServletResponse response) {
        CheckUtil.notNull(file, "上传的Excel数据文件为空");
        CheckUtil.notEmpty(req.getExcelType(), "Excel类型不能为空");
        return new ResultBean<>(dataBiz.supplierImport(file, req, response));
    }


    @ApiOperation(value = "取消合作", httpMethod = "PUT", notes = "取消合作")
    @PutMapping(value = "/cancel/agreement")
    public ResultBean<String> delete(@RequestBody OrderReq req) {
        CheckUtil.notNull(req.getWorkOrderIds(), "工单数据id不能为空");
        return new ResultBean<>(dataBiz.delete(req.getWorkOrderIds()));
    }

//    @ApiOperation(value = "违约记录列表", httpMethod = "GET", notes = "违约记录列表")
//    @GetMapping(value = "/lostPromise/list/{workOrderId}")
//    public ResultBean<List<WorkOrderDataResp>> lostPromiseList(@PathVariable("workOrderId") String workOrderId) {
//        CheckUtil.notNull(workOrderId, "工单ID不能为空");
//        return new ResultBean<>(dataBiz.lostPromiseList(workOrderId));
//    }

    @ApiOperation(value = "违约", httpMethod = "PUT", notes = "违约")
    @PutMapping(value = "/lostPromise")
    public ResultBean<String> lostPromise(@RequestBody OrderReq req) {
        CheckUtil.notEmpty(req.getWorkOrderDataId(), "工单id不能为空");
        CheckUtil.notEmpty(req.getPrice(), "违约金不能为空");
        return new ResultBean<>(dataBiz.lostPromise(req.getWorkOrderDataId(), req.getPrice()));
    }

    @ApiOperation(value = "重新制作", httpMethod = "PUT", notes = "重新制作")
    @PutMapping(value = "/remake")
    public ResultBean<String> remake(@RequestBody OrderReq req) {
        CheckUtil.notEmpty(req.getWorkOrderDataId(), "工单id不能为空");
        CheckUtil.notEmpty(req.getPrice(), "制作费用不能为空");
        return new ResultBean<>(dataBiz.remake(req.getWorkOrderDataId(), req.getPrice()));
    }

}

