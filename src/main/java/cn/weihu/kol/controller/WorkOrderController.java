package cn.weihu.kol.controller;


import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工单表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@RestController
@RequestMapping(value = "workorder")
@Api(value = "工单管理", tags = "工单管理")
public class WorkOrderController {

    @Autowired
    private WorkOrderBiz workOrderBiz;

    @ApiOperation(value = "列表", httpMethod = "GET", notes = "列表")
    @GetMapping(value = "/page")
    public ResultBean<PageResult<WorkOrderResp>> workOrderPage(WorkOrderReq req) {
        return new ResultBean<>(workOrderBiz.workOrderPage(req));
    }


}

