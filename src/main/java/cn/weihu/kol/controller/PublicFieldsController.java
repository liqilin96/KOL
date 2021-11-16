package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.PublicFieldsBiz;
import cn.weihu.kol.db.po.PublicFields;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.req.PublicFieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字段字典表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-11-16
 */
@RestController
@RequestMapping("/publicFields")
@Api(value = "公共字段配置", tags = "公共字段配置")
public class PublicFieldsController {


    @Autowired
    private PublicFieldsBiz publicFieldsBiz;


    @ApiOperation(value = "创建公共字段", httpMethod = "POST", notes = "创建公共字段")
    @PostMapping(value = "/create")
    public ResultBean<String> create(@RequestBody PublicFieldsReq req) {
        CheckUtil.notEmpty(req.getName(), "公共字段组名不能为空");
        CheckUtil.notEmpty(req.getFieldList(), "公共字段列表不能为空");
        return new ResultBean<>(publicFieldsBiz.create(req));
    }

    @ApiOperation(value = "修改公共字段", httpMethod = "PATCH", notes = "修改公共字段")
    @PatchMapping(value = "/update")
    public ResultBean<String> update(@RequestBody FieldsReq req) {
        CheckUtil.notEmpty(req.getId(), "公共字段组id不能为空");
        return new ResultBean<>(publicFieldsBiz.update(req));
    }

    @ApiOperation(value = "删除公共字段", httpMethod = "GET", notes = "删除公共字段")
    @DeleteMapping(value = "/delete")
    public ResultBean<String> delete(@RequestBody FieldsReq req) {
        CheckUtil.notEmpty(req.getName(), "公共字段组id不能为空");
        return new ResultBean<>(publicFieldsBiz.delete(req));
    }

    @ApiOperation(value = "查询公共字段", httpMethod = "GET", notes = "查询公共字段")
    @GetMapping(value = "/query")
    public ResultBean<List<FieldsResp>> page(FieldsReq req) {
        return new ResultBean<>(publicFieldsBiz.query(req));
    }


}

