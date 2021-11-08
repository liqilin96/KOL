package cn.weihu.kol.controller;


import cn.hutool.db.PageResult;
import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.RoleBiz;
import cn.weihu.kol.http.req.RoleListReq;
import cn.weihu.kol.http.req.RoleSaveReq;
import cn.weihu.kol.http.resp.RoleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@RestController
@RequestMapping(value = "role")
@Api(value = "角色管理", tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleBiz roleBiz;

    @ApiOperation(value = "角色列表-分页", httpMethod = "GET", notes = "角色列表-分页")
    @GetMapping(value = "/page")
    public ResultBean<PageResult<RoleResp>> rolePage(RoleListReq req) {
        return new ResultBean(roleBiz.rolePage(req));
    }

    @ApiOperation(value = "角色列表-全部", httpMethod = "GET", notes = "角色列表-全部")
    @GetMapping(value = "/list")
    public ResultBean<List<RoleResp>> roleAll() {
        return new ResultBean<>(roleBiz.roleAll());
    }

    @ApiOperation(value = "新增", httpMethod = "POST", notes = "新增")
    @PostMapping(value = "")
    public ResultBean<String> roleAdd(@RequestBody RoleSaveReq req) {
        CheckUtil.notEmpty(req.getName(), "角色名称不能为空");
        return new ResultBean<>(roleBiz.roleAdd(req));
    }

    @ApiOperation(value = "编辑", httpMethod = "PUT", notes = "编辑")
    @PutMapping(value = "/{id}")
    public ResultBean<String> roleEdit(@PathVariable String id, @RequestBody RoleSaveReq req) {
        CheckUtil.notEmpty(req.getName(), "角色名称不能为空");
        return new ResultBean<>(roleBiz.roleEdit(id, req));
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @DeleteMapping(value = "/{id}")
    public ResultBean<String> roleDel(@PathVariable String id) {
        return new ResultBean<>(roleBiz.roleDel(id));
    }
}

