package cn.weihu.kol.biz.impl;

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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

        Project project = new Project();
        project.setName(req.getName());
        project.setDescs(req.getDesc());
        project.setCtime(LocalDateTime.now());
        project.setUtime(LocalDateTime.now());
        project.setCreateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        project.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        this.baseMapper.insert(project);

        //创建工单
        if(req.getWorkOrderReq() != null) {
            insertDBExecutors.execute(() -> {

                WorkOrder workOrder = new WorkOrder();

                //TODO  创建工单和工单详情没做，待续，没时间做！！！
                workOrder.setType(0L);
                workOrder.setCtime(LocalDateTime.now());
                workOrder.setProjectId(project.getId());
                workOrderBiz.save(workOrder);
            });

        }
        return null;
    }

    @Override
    public String updateProjectName(ProjectReq req) {
//        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Project::getName,req.getName());
        Project project = this.baseMapper.selectOne(new LambdaQueryWrapper<Project>().eq(Project::getName, req.getName()));
        if(project != null) {
            throw new CheckException("修改失败，项目名已经存在");
        }

        project = new Project();

        project.setId(Long.parseLong(req.getId()));
        project.setName(req.getNewName());
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
            wrapper.eq(Project::getName, req.getName());
        }

        if(req.getStartTime() != null && req.getEndTime() != null) {
            wrapper.between(Project::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }

        wrapper.orderByDesc(Project::getCtime);

        Page<Project> projectPage = this.baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<ProjectResp> resps = projectPage.getRecords().stream().map(x -> {
            ProjectResp resp = new ProjectResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());

        return new PageResult<>(projectPage.getTotal(), resps);
    }
}
