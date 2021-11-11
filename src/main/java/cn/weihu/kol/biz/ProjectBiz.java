package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Project;
import cn.weihu.kol.http.req.ProjectReq;
import cn.weihu.kol.http.resp.ProjectResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface ProjectBiz extends IService<Project> {

    String create(ProjectReq req);

    String updateProjectName(ProjectReq req);

    String deleteProject(String id);

    PageResult<ProjectResp> pages(ProjectReq req);
}
