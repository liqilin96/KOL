package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;
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


    @ApiOperation(value = "创建字段组", httpMethod = "POST", notes = "创建字段")
    @PostMapping(value = "/create/group")
    public ResultBean<String> createFieldGroup(@RequestBody FieldsReq req) {
        CheckUtil.notEmpty(req.getName(), "字段组名不能为空");
        CheckUtil.notEmpty(req.getFieldList(), "字段列表不能为空");
        return new ResultBean<>(fieldsBiz.createGroup(req));
    }

    @ApiOperation(value = "修改字段组", httpMethod = "PATCH", notes = "修改字段组")
    @PatchMapping(value = "/update/group")
    public ResultBean<String> updateFieldGroup(@RequestBody FieldsReq req) {
        CheckUtil.notEmpty(req.getId(), "字段组id不能为空");
        return new ResultBean<>(fieldsBiz.updateGroup(req));
    }

    @ApiOperation(value = "删除字段组", httpMethod = "GET", notes = "删除字段组")
    @DeleteMapping(value = "/delete/group")
    public ResultBean<String> deleteGroup(@RequestBody FieldsReq req) {
        CheckUtil.notEmpty(req.getName(), "字段组id不能为空");
        return new ResultBean<>(fieldsBiz.deleteGroup(req));
    }

    @ApiOperation(value = "查询字段组", httpMethod = "GET", notes = "查询字段组")
    @GetMapping(value = "/query/page")
    public ResultBean<PageResult<FieldsResp>> page(FieldsReq req) {
        return new ResultBean<>(fieldsBiz.pageGroup(req));
    }


//    @ApiOperation(value = "删除字段组", httpMethod = "GET", notes = "删除字段组")
//    @GetMapping(value = "/query/group")
//    public ResultBean<String> deleteGroup(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getName(), "字段组id不能为空");
//        return new ResultBean<>(fieldsBiz.deleteGroup(req));
//    }
//
//    @ApiOperation(value = "创建字段组", httpMethod = "POST", notes = "创建字段")
//    @PostMapping(value = "/create/group")
//    public ResultBean<String> createFieldGroup(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getName(), "字段组名不能为空");
//        CheckUtil.notEmpty(req.getType(), "字段类型不能为空");
//        return new ResultBean<>(fieldsBiz.createGroup(req));
//    }
//
//    @ApiOperation(value = "修改字段组", httpMethod = "PATCH", notes = "修改字段组")
//    @PatchMapping(value = "/update/group")
//    public ResultBean<String> updateFieldGroup(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getId(), "要修改的字段组id不能为空");
//        CheckUtil.notEmpty(req.getNewName(), "要修改的字段组名不能为空");
//        return new ResultBean<>(fieldsBiz.updateGroup(req));
//    }
//
//    @ApiOperation(value = "查询字段组", httpMethod = "GET", notes = "查询字段组")
//    @GetMapping(value = "/query/page")
//    public ResultBean<PageResult<FieldsResp>> page(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getType(), "查询类型不能为空");
//        return new ResultBean<>(fieldsBiz.pageGroup(req));
//    }
//
//
//    @ApiOperation(value = "创建字段", httpMethod = "POST", notes = "创建字段")
//    @PostMapping(value = "/create")
//    public ResultBean<String> createField(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getFieldList(), "字段列表不能为空");
//        CheckUtil.notEmpty(req.getType(), "字段类型不能为空");
//        return new ResultBean<>(fieldsBiz.create(req));
//    }
//
//
//    @ApiOperation(value = "修改字段", httpMethod = "PATCH", notes = "修改字段")
//    @PatchMapping(value = "/update")
//    public ResultBean<String> updateField(@RequestBody FieldsReq req) {
//        CheckUtil.notEmpty(req.getId(), "要修改的字段id不能为空");
//        CheckUtil.notEmpty(req.getFieldList(), "要修改的字段列表不能为空");
//        return new ResultBean<>(fieldsBiz.updateField(req));
//    }

}

