package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.UserBiz;
import cn.weihu.kol.http.req.LoginReq;
import cn.weihu.kol.http.req.ModifyPasswordReq;
import cn.weihu.kol.http.resp.LoginResp;
import cn.weihu.kol.http.resp.UserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@RestController
@RequestMapping("")
@Api(value = "用户管理", tags = "用户管理")
public class UserController {

    @Autowired
    private UserBiz userBiz;

    @ApiOperation(value = "登录", httpMethod = "POST", notes = "登录notes")
    @PostMapping(value = "/login")
    public ResultBean<LoginResp> login(@RequestBody LoginReq req, HttpServletRequest request) {
        CheckUtil.notEmpty(req.getUsername(), "用户名不能为空");
        CheckUtil.notEmpty(req.getPassword(), "密码不能为空");
        return new ResultBean<>(userBiz.login(req, request));
    }

    @ApiOperation(value = "登出", httpMethod = "POST", notes = "登出notes")
    @PostMapping(value = "/logout")
    public ResultBean<String> logout(HttpServletRequest request) {
        userBiz.delToken(request);
        return new ResultBean();
    }

    @ApiOperation(value = "修改密码", httpMethod = "PUT", notes = "修改密码notes")
    @PutMapping(value = "/password")
    public ResultBean<String> modifyPassword(@RequestBody ModifyPasswordReq req) {
        CheckUtil.notEmpty(req.getOldPassword(), "原密码不能为空");
        CheckUtil.notEmpty(req.getPassword(), "新密码不能为空");
        return new ResultBean<>(userBiz.modifyPassword(req));
    }

    @ApiOperation(value = "列表-不分页", httpMethod = "GET", notes = "列表-不分页")
    @GetMapping(value = "/user/list")
    public ResultBean<List<UserResp>> userList() {
        return new ResultBean<>(userBiz.userList());
    }


}

