package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.ProjectBiz;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.db.dao.ProjectDao;
import cn.weihu.kol.db.po.Project;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.ProjectReq;
import cn.weihu.kol.http.resp.ProjectResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class ProjectBizImpl extends ServiceImpl<ProjectDao, Project> implements ProjectBiz {

    @Autowired
    private WorkOrderBiz workOrderBiz;

    ExecutorService insertDBExecutors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Override
    public String create(ProjectReq req) {

        if(!req.getBudget().matches("[1-9]{1}[0-9]{0,}|[1-9]{1}[0-9]{0,}\\.[0-9]{2}") || req.getBudget().length() > 18) {
            throw new CheckException("预算输入有误或者超出最大范围");
        }

        Project project = new Project();
        project.setName(req.getName());
        project.setDescs(req.getDesc());
        project.setBudget(Double.parseDouble(req.getBudget()));
        project.setProjectImg(req.getProjectImg());
        project.setCtime(new Date());
        project.setUtime(new Date());
        project.setCreateUserId(UserInfoContext.getUserId());
        project.setUpdateUserId(UserInfoContext.getUserId());
        this.baseMapper.insert(project);
        return project.getId() + "";
    }

    @Override
    public String updateProject(ProjectReq req) {
        Project project = getById(req.getId());
        if(project == null) {
            throw new CheckException("修改失败，项目不存在");
        }
        if(req.getBudget() != null && (!req.getBudget().matches("[1-9]{1}[0-9]{0,}|[1-9]{1}[0-9]{0,}\\.[0-9]{2}") || req.getBudget().length() > 18)) {
            throw new CheckException("预算输入有误或者超出最大范围");
        }
        project.setDescs(req.getDesc());
        project.setProjectImg(req.getProjectImg());
        project.setBudget(req.getBudget() == null ? null : Double.parseDouble(req.getBudget()));
        project.setUtime(new Date());
        project.setUpdateUserId(UserInfoContext.getUserId());
        this.baseMapper.updateById(project);
        return null;
    }


    @Override
    public String deleteProject(String id) {
        //只能删除空项目
        List<WorkOrder> workOrders = workOrderBiz.list(new QueryWrapper<WorkOrder>().eq("project_id", id));
        if(workOrders != null && workOrders.size() > 0) {
            throw new CheckException("项目无法删除，请先删除项目内工单");
        }
        this.baseMapper.deleteById(id);
        return null;
    }

    @Override
    public PageResult<ProjectResp> pages(ProjectReq req) {

        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.like(Project::getName, req.getName());
        }

        if(req.getStartTime() != null && req.getEndTime() != null) {
            wrapper.between(Project::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }

        wrapper.ne(Project::getName, "保价即将到期")
                .orderByDesc(Project::getUtime);

        Page<Project> projectPage = this.baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<ProjectResp> resps = projectPage.getRecords().stream().map(x -> {
            ProjectResp resp = new ProjectResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());

        return new PageResult<>(projectPage.getTotal(), resps);
    }

    @Override
    public String importProjectImg(MultipartFile file, String projectName) {
        String fileName    = file.getOriginalFilename();
        String contentType = StringUtils.substringAfterLast(fileName, ".");
        //对立项单不校验
//        if(!contentType.equalsIgnoreCase("jpg") && !contentType.equalsIgnoreCase("png")) {
//            throw new CheckException("图片格式上传错误");
//        }
        String filePath    = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator;
        String newFileName = UUID.randomUUID() + "." + contentType;
        String path        = null;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, newFileName);
            path = (filePath + newFileName).replace("\\", "/");
        } catch(Exception e) {
            e.printStackTrace();
            throw new CheckException("上传立项单失败");
        }
        return path;
    }

    @Override
    public void downloadProjectImg(String path, HttpServletResponse response) {
        FileUtil.download(response, path, false);
    }
}
