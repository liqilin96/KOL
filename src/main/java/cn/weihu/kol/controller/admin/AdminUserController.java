package cn.weihu.kol.controller.admin;

import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.UserBiz;
import cn.weihu.kol.http.req.UserListReq;
import cn.weihu.kol.http.req.UserSaveReq;
import cn.weihu.kol.http.resp.UserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "admin")
@Api(value = "用户管理-admin", tags = "用户管理-admin")
public class AdminUserController {

    @Autowired
    private UserBiz userBiz;

    @ApiOperation(value = "用户列表-分页", httpMethod = "GET", notes = "用户列表notes-分页")
    @GetMapping(value = "/user/page")
    public ResultBean<PageResult<UserResp>> userPage(UserListReq req) {
        return new ResultBean<>(userBiz.userPage(req));
    }

    @ApiOperation(value = "添加用户", httpMethod = "POST", notes = "添加用户notes")
    @PostMapping(value = "/user")
    public ResultBean<String> addUser(@RequestBody UserSaveReq req) {
        CheckUtil.notEmpty(req.getUsername(), "用户名不能为空");
        CheckUtil.notNull(req.getRoleIds(), "角色不能为空");
        return new ResultBean<>(userBiz.addUser(req));
    }

    @ApiOperation(value = "编辑用户", httpMethod = "PUT", notes = "编辑用户notes")
    @PutMapping(value = "/user/{id}")
    public ResultBean<String> editUser(@PathVariable("id") String id, @RequestBody UserSaveReq req) {
        CheckUtil.notEmpty(req.getUsername(), "用户名不能为空");
        CheckUtil.notNull(req.getRoleIds(), "角色不能为空");
        return new ResultBean<>(userBiz.editUser(id, req));
    }

    @ApiOperation(value = "重置密码", httpMethod = "PUT", notes = "重置密码notes")
    @PutMapping(value = "/user/password/reset/{id}")
    public ResultBean<String> resetPassword(@PathVariable String id) {
        return new ResultBean<>(userBiz.resetPassword(id));
    }

}
