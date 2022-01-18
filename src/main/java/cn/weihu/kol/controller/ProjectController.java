package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.ProjectBiz;
import cn.weihu.kol.http.req.ProjectReq;
import cn.weihu.kol.http.resp.ProjectResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/project")
@Api(value = "项目管理", tags = "项目管理")
public class ProjectController {

    @Autowired
    private ProjectBiz projectBiz;


    @ApiOperation(value = "创建项目", httpMethod = "POST", notes = "创建项目")
    @PostMapping(value = "/create")
    public ResultBean<String> create(@RequestBody ProjectReq req) {
        CheckUtil.notEmpty(req.getName(), "项目名不能为空");
        CheckUtil.notEmpty(req.getDesc(), "项目描述不能为空");
        CheckUtil.notEmpty(req.getBudget(), "项目预算不能为空");
        CheckUtil.notEmpty(req.getProjectImg(), "项目立项单不能为空");
        return new ResultBean<>(projectBiz.create(req));
    }

    @ApiOperation(value = "修改项目", httpMethod = "PATCH", notes = "修改项目名")
    @PatchMapping(value = "/update")
    public ResultBean<String> update(@RequestBody ProjectReq req) {
        CheckUtil.notEmpty(req.getId(), "要修改的项目id不能为空");
        return new ResultBean<>(projectBiz.updateProject(req));
    }

    @ApiOperation(value = "上传立项单", httpMethod = "POST", notes = "上传立项单")
    @PostMapping(value = "/import")
    public ResultBean<String> importProjectImg(@RequestParam("file") MultipartFile file,ProjectReq req) {
        CheckUtil.notNull(file, "立项单不能为空");
        return new ResultBean<>(projectBiz.importProjectImg(file,req.getName()));
    }

    @ApiOperation(value = "下载立项单", httpMethod = "GET", notes = "下载立项单")
    @GetMapping(value = "/download")
    public void downloadPDF(String path, HttpServletResponse response) {
        CheckUtil.notNull(path, "立项单文件地址不能为空");
        projectBiz.downloadProjectImg(path, response);
    }

    @ApiOperation(value = "删除项目", httpMethod = "PATCH", notes = "修改项目名")
    @PatchMapping(value = "/update/{id}")
    public ResultBean<String> delete(@PathVariable("id") String id) {
        CheckUtil.notEmpty(id, "要删除的项目id不能为空");
        return new ResultBean<>(projectBiz.deleteProject(id));
    }

    @ApiOperation(value = "项目查询", httpMethod = "GET", notes = "项目查询")
    @GetMapping(value = "/query/page")
    public ResultBean<PageResult<ProjectResp>> page(ProjectReq req) {
        return new ResultBean<>(projectBiz.pages(req));
    }

}

