package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.convert.WorkOrderConverter;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.ExcelUtils;
import cn.weihu.kol.util.ExceptionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class WorkOrderBizImpl extends ServiceImpl<WorkOrderDao, WorkOrder> implements WorkOrderBiz {

    @Override
    public ResultBean<WorkOrderResp> ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response) {
        ResultBean<WorkOrderResp> resultBean = new ResultBean<>();
        WorkOrderResp             resp       = new WorkOrderResp();

        try {
            //校验文件类型
            if(!file.getOriginalFilename().endsWith("xls") && !file.getOriginalFilename().endsWith("xlsx")) {
                log.error(file.getOriginalFilename() + "不是excel文件");
                throw new IOException(file.getOriginalFilename() + "不是excel文件");
            }

            List<Object> data = ExcelUtils.readMoreThan1000Row(file.getInputStream());

//            //读取到的表头
//            String excel = data.get(0);

        } catch(Exception e) {
            resultBean.setData(null);
            resultBean.setCode(ResultBean.FAIL);
            resultBean.setMsg(ExceptionUtil.getMessage(e));
            return resultBean;
        }

        return null;
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtils.downloadLocal(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req) {
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.lambdaQuery(WorkOrder.class);
        wrapper.eq(Objects.nonNull(req.getProjectId()), WorkOrder::getProjectId, req.getProjectId())
                .and(StringUtils.isNotBlank(req.getName()), workOrderLambdaQueryWrapper -> workOrderLambdaQueryWrapper
                        .like(WorkOrder::getName, "%" + req.getName() + "%")
                        .or()
                        .eq(WorkOrder::getOrderSn, req.getName()))
                .eq(StringUtils.isNotBlank(req.getStatus()), WorkOrder::getStatus, req.getStatus())
                .between(Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime()),
                         WorkOrder::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        wrapper.eq(WorkOrder::getType, 1);
        Page<WorkOrder> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<WorkOrderResp> respList = page.getRecords().stream()
                .map(WorkOrderConverter::entity2WorkOrderResp)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public Long create(WorkOrder workOrder) {
        if(Objects.isNull(workOrder.getProjectId())) {
            throw new CheckException("项目ID不能为空");
        }
        Integer count = baseMapper.getCount(workOrder.getProjectId());
        count = count + 1;
        workOrder.setName("第" + count + "批次");
        workOrder.setOrderSn(DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_MS_PATTERN));
        workOrder.setType(Constants.WORK_ORDER_DEMAND);
        workOrder.setStatus(Constants.WORK_ORDER_NEW);
        workOrder.setCtime(DateUtil.date());
        workOrder.setUtime(DateUtil.date());
        workOrder.setCreateUserId(UserInfoContext.getUserId());
        workOrder.setUpdateUserId(UserInfoContext.getUserId());
        //
        save(workOrder);
        return workOrder.getId();
    }
}
