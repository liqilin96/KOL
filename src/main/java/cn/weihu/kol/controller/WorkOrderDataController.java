package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.http.req.WorkOrderBatchUpdateReq;
import cn.weihu.kol.http.req.WorkOrderDataReq;
import cn.weihu.kol.http.req.WorkOrderDataReviewReq;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class WorkOrderDataController {

    @Autowired
    private WorkOrderDataBiz dataBiz;

    @ApiOperation(value = "数据列表", httpMethod = "GET", notes = "数据列表")
    @GetMapping(value = "/list")
    public ResultBean<List<WorkOrderDataResp>> workOrderDataList(WorkOrderDataReq req) {
        CheckUtil.notNull(req.getWorkOrderId(), "工单ID不能为空");
        return new ResultBean<>(dataBiz.workOrderDataList(req));
    }

    @ApiOperation(value = "批量更新", httpMethod = "PUT", notes = "批量更新")
    @PutMapping(value = "/update/batch")
    public ResultBean<String> updateWorkOrder(@RequestBody WorkOrderBatchUpdateReq req) {
        return new ResultBean<>(dataBiz.updateWorkOrder(req));
    }

    @ApiOperation(value = "库内外筛选", httpMethod = "POST", notes = "库内外筛选")
    @PutMapping(value = "/screening")
    public ResultBean<WorkOrderDataScreeningResp> screening(@RequestBody WorkOrderBatchUpdateReq req) {
        return new ResultBean<>(dataBiz.screening(req));
    }

    @ApiOperation(value = "询价&询档", httpMethod = "POST", notes = "询价&询档")
    @PutMapping(value = "/enquiry")
    public ResultBean<String> enquiry(@RequestBody WorkOrderBatchUpdateReq req) {
        return new ResultBean<>(dataBiz.enquiry(req));
    }

    @ApiOperation(value = "报价", httpMethod = "POST", notes = "报价")
    @PutMapping(value = "/quote")
    public ResultBean<String> quote(@RequestBody WorkOrderBatchUpdateReq req) {
        return new ResultBean<>(dataBiz.quote(req));
    }

    @ApiOperation(value = "审核", httpMethod = "POST", notes = "审核")
    @PutMapping(value = "/review")
    public ResultBean<String> review(@RequestBody WorkOrderDataReviewReq req) {
        CheckUtil.notEmpty(req.getStatus(), "审核结果状态不能为空");
        return new ResultBean<>(dataBiz.review(req));
    }
}

