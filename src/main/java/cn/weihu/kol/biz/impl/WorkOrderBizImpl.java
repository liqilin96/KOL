package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.ProjectBiz;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.convert.WorkOrderConverter;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Project;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.FileUtil;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
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


    @Autowired
    private WorkOrderDataBiz workOrderDataBiz;

    @Autowired
    private FieldsBiz fieldsBiz;

    @Autowired
    private ProjectBiz projectBiz;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response) {

        //校验文件类型
        if(!file.getOriginalFilename().endsWith("xls") && !file.getOriginalFilename().endsWith("xlsx")) {
            log.error(file.getOriginalFilename() + "不是excel文件");
            throw new CheckException(file.getOriginalFilename() + "不是excel文件");
        }

        List<Object> orderBos = null;
        try {
            orderBos = EasyExcelUtil.readExcel(file.getInputStream());
        } catch(Exception e) {
            log.error(">>> Excel读取失败:{}", e);
            throw new CheckException("Excel读取失败,请联系管理员");
        }

        if(null != orderBos) {
            // 判断表头
            LinkedHashMap<Integer, String> title      = (LinkedHashMap<Integer, String>) orderBos.get(0);
            List<String>                   list       = title.values().stream().collect(Collectors.toList());
            String                         excelTitle = list.toString();

            List<String> selfTitle = excelTitle();
            if(!selfTitle.toString().equalsIgnoreCase(excelTitle)) {
                throw new CheckException("Excel文件标题不匹配");
            }
        }

        WorkOrder workOrder = new WorkOrder();

        try {
            String        name          = "示例数据";
            WorkOrderData workOrderData = null;

            if(orderBos != null) {
                Project project = projectBiz.getById(req.getProjectId());
                workOrder.setProjectId(project.getId());
                workOrder.setProjectName(project.getName());
                create(workOrder, Constants.WORK_ORDER_DEMAND, Constants.WORK_ORDER_NEW);
            }
            for(int x = 1; x < orderBos.size(); x++) {
                LinkedHashMap<Integer, String> bo = (LinkedHashMap<Integer, String>) orderBos.get(x);
                /**具体配置如下
                 *
                 *  0     -----》序号
                 *  1     -----》媒体
                 *  2     -----》账号
                 *  3     -----》账号ID或链接
                 *  4     -----》账号类型
                 //                 *  5     -----》粉丝数
                 *  5     -----》资源位置
                 *  6     -----》数量
                 *  7     -----》发布开始时间
                 *  8     -----》发布结束时间
                 *  9    -----》电商链接
                 *  10    -----》@
                 *  11    -----》话题
                 *  12    -----》电商肖像授权
                 *  13    -----》品牌双微转发授权
                 *  14    -----》微任务
                 *  15    -----》其他
                 *  16    -----》产品提供方
                 *  17    -----》发布内容brief概述
                 */
                //获取第一条数据的账号，如果等于示例数据则跳过
                String accountName = bo.get(2);
                if(name.equalsIgnoreCase(accountName)) {
                    continue;
                }
                workOrderData = new WorkOrderData();

                workOrderData.setProjectId(workOrder.getProjectId());
                workOrderData.setWorkOrderId(workOrder.getId());
                //字段组3报价单
                workOrderData.setFieldsId(3L);

                Fields fields = fieldsBiz.getById(3);
                //获取字段列表
                List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
                }.getType());

                Map<String, String> map   = new HashMap<>();
                List<String>        title = excelTitle();
                for(int i = 0; i < fieldsBos.size(); i++) {
                    for(int j = 0; j < title.size(); j++) {
                        if(title.get(j).equalsIgnoreCase(fieldsBos.get(i).getTitle())) {
                            map.put(fieldsBos.get(i).getDataIndex(), bo.get(j));
                        }
                    }
//                    if(title.contains(fieldsBos.get(i).getTitle())) {
//                        map.put(fieldsBos.get(i).getDataIndex(), bo.get(i));
//                    }
                }
                workOrderData.setStatus(Constants.WORK_ORDER_DATA_NEW);
                workOrderData.setData(GsonUtils.gson.toJson(map));
                workOrderData.setCtime(new Date());
                workOrderData.setUtime(new Date());
                workOrderData.setCreateUserId(UserInfoContext.getUserId());
                workOrderData.setUpdateUserId(UserInfoContext.getUserId());
                workOrderDataBiz.save(workOrderData);
            }
        } catch(Exception e) {
            log.error(">>> 数据导入异常:", e);
            throw new CheckException("数据加载异常,请联系管理员");
        }
        return workOrder.getId().toString();
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        try {
            EasyExcelUtil.downloadLocal(response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req) {
        if(StringUtils.isNotBlank(req.getStatus()) &&
           !StringUtils.equalsAny(req.getStatus(), Constants.WORK_ORDER_NEW, Constants.WORK_ORDER_ASK, Constants.WORK_ORDER_QUOTE,
                                  Constants.WORK_ORDER_REVIEW, Constants.WORK_ORDER_ORDER)) {
            throw new CheckException("工单状态不合法");
        }
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.lambdaQuery(WorkOrder.class);
        wrapper.eq(Objects.nonNull(req.getProjectId()), WorkOrder::getProjectId, req.getProjectId())
                .and(StringUtils.isNotBlank(req.getName()), workOrderLambdaQueryWrapper -> workOrderLambdaQueryWrapper
                        .like(WorkOrder::getName, req.getName())
                        .or()
                        .likeRight(WorkOrder::getOrderSn, req.getName()))
                .eq(StringUtils.isNotBlank(req.getStatus()), WorkOrder::getStatus, req.getStatus());
        if(Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime())) {
            wrapper.between(WorkOrder::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }
        wrapper.isNull(WorkOrder::getParentId);
        Page<WorkOrder> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<WorkOrderResp> respList = page.getRecords().stream()
                .map(WorkOrderConverter::entity2WorkOrderResp)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public WorkOrder create(WorkOrder workOrder, Integer type, String status) {
        if(Objects.isNull(workOrder.getProjectId())) {
            throw new CheckException("项目ID不能为空");
        }
        Integer count = baseMapper.getCount(workOrder.getProjectId());
        count = count + 1;
        workOrder.setName("第" + count + "批次");
        workOrder.setOrderSn(DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_MS_PATTERN));
        workOrder.setType(type);
        workOrder.setStatus(status);
        workOrder.setCtime(DateUtil.date());
        workOrder.setUtime(DateUtil.date());
        workOrder.setCreateUserId(UserInfoContext.getUserId());
        workOrder.setUpdateUserId(UserInfoContext.getUserId());
        //
        save(workOrder);
        return workOrder;
    }

    @Override
    public PageResult<WorkOrderResp> waitWorkOrderPage(WorkOrderReq req) {
        if(!StringUtils.equalsAny(req.getStatus(), Constants.WORK_ORDER_ASK, Constants.WORK_ORDER_REVIEW)) {
            throw new CheckException("工单状态不合法");
        }
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.lambdaQuery(WorkOrder.class);
        wrapper.eq(WorkOrder::getToUser, UserInfoContext.getUserId())
                .eq(WorkOrder::getStatus, req.getStatus());
        Page<WorkOrder> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<WorkOrderResp> respList = page.getRecords().stream()
                .map(WorkOrderConverter::entity2WorkOrderResp)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public String importPicture(MultipartFile file) {

        String fileName    = file.getOriginalFilename();
        String contentType = StringUtils.substringAfterLast(fileName, ".");
        if(!contentType.equalsIgnoreCase("jpg") && !contentType.equalsIgnoreCase("png")) {
            throw new CheckException("图片格式上传错误");
        }
        String filePath    = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator;
        String newFileName = UUID.randomUUID() + "." + contentType;
        String path        = null;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, newFileName);
            path = (filePath + newFileName).replace("\\", "/");
        } catch(Exception e) {
            e.printStackTrace();
            throw new CheckException("上传图片失败");
        }
        return path;
    }

    @Override
    public void downloadPicTure(String picturePath, HttpServletResponse response) {

        FileUtil.download(response, picturePath, false);
    }

    public List<String> excelTitle() {
        return Arrays.asList("序号", "媒体", "账号", "账号ID或链接", "账号类型", "资源位置", "数量", "发布开始时间",
                             "发布结束时间", "电商链接", "@", "话题", "电商肖像授权", "品牌双微转发授权", "微任务", "其他",
                             "产品提供方", "发布内容brief概述");
    }
}
