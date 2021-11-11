package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.req.ProjectReq;
import cn.weihu.kol.http.resp.ProjectResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字段组表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/fields")
@Api(value = "字段配置", tags = "字段配置")
public class FieldsController {


    @Autowired
    private FieldsBiz fieldsBiz;

    @ApiOperation(value = "创建字段组", httpMethod = "POST", notes = "创建字段组")
    @PostMapping(value = "/create")
    public ResultBean<String> create(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getName(), "字段组名不能为空");
        CheckUtil.notEmpty(req.getFieldList(), "字段列表不能为空");
        CheckUtil.notEmpty(req.getType(), "字段类型不能为空");
        return new ResultBean<>(fieldsBiz.create(req));
    }


//    @ApiOperation(value = "字段组查询", httpMethod = "GET", notes = "字段组查询")
//    @GetMapping(value = "/query/page")
//    public ResultBean<PageResult<ProjectResp>> page(@RequestBody ProjectReq req) {
//        return new ResultBean<>(fieldsBiz.pages(req));
//    }
}

