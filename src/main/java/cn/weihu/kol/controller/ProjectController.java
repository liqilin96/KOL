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
        return new ResultBean<>(projectBiz.create(req));
    }

    @ApiOperation(value = "修改项目名", httpMethod = "PATCH", notes = "修改项目名")
    @PatchMapping(value = "/update")
    public ResultBean<String> update(@RequestBody ProjectReq req) {
        CheckUtil.notEmpty(req.getId(), "要修改的项目id不能为空");
        CheckUtil.notEmpty(req.getNewName(), "要修改的项目名不能为空");
        return new ResultBean<>(projectBiz.updateProjectName(req));
    }


    @ApiOperation(value = "删除项目名", httpMethod = "PATCH", notes = "修改项目名")
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

