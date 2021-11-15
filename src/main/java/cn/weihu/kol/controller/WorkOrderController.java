package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 工单表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/workorder")
@Api(value = "工单管理", tags = "工单管理")
public class WorkOrderController {


    @Autowired
    private WorkOrderBiz workOrderBiz;


    @ApiOperation(value = "导入", httpMethod = "POST", notes = "导入")
    @PostMapping("/import")
    public ResultBean<WorkOrderResp> ImportData(@RequestParam("file") MultipartFile file, WorkOrderReq req, HttpServletResponse response) {
        CheckUtil.notNull(file, "上传的Excel数据文件为空");
        return workOrderBiz.ImportData(file,req,response);
    }
}

