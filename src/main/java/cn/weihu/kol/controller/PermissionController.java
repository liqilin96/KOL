package cn.weihu.kol.controller;


import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.PermissionBiz;
import cn.weihu.kol.http.resp.PermissionResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@RestController
@RequestMapping(value = "permission")
@Api(value = "权限管理", tags = "权限管理")
public class PermissionController {

    @Autowired
    private PermissionBiz permissionBiz;

    @ApiOperation(value = "列表", httpMethod = "GET", notes = "列表")
    @GetMapping(value = "")
    public ResultBean<List<PermissionResp>> permissionList() {
        return new ResultBean<>(permissionBiz.permissionList());
    }
}

