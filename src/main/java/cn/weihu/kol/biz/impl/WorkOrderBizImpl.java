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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            orderBos = EasyExcelUtil.readExcelOnlySheet1(file.getInputStream());
        } catch(Exception e) {
            log.error(">>> Excel读取失败:{}", e);
            throw new CheckException("Excel读取失败,请联系管理员");
        }

        List<String> selfTitle = excelTitle(req.getExcelType());
        if(null != orderBos) {
            // 判断表头             数字9是读取模板标题名称（可能会改动）
            LinkedHashMap<Integer, String> title = (LinkedHashMap<Integer, String>) orderBos.get(9);
            List<String>                   list  = title.values().stream().collect(Collectors.toList());
            if(list.size() < selfTitle.size()) {
                throw new CheckException("Excel文件标题不匹配,请勿修改或重新下载模版");
            }
            //截取需要填写的字段
            List<String> titleList = list.subList(0, selfTitle.size());
            if(!selfTitle.toString().equalsIgnoreCase(titleList.toString())) {
                throw new CheckException("Excel文件标题不匹配,请勿修改或重新下载模版");
            }
        }
        WorkOrder workOrder = new WorkOrder();
        try {
//            String        name          = "示例数据";
            WorkOrderData workOrderData = null;

            if(orderBos != null) {
                Project project = projectBiz.getById(req.getProjectId());
                workOrder.setProjectId(project.getId());
                workOrder.setProjectName(project.getName());
                create(workOrder, Constants.WORK_ORDER_DEMAND, Constants.WORK_ORDER_NEW);
            }
            // x = 10 ===》 模板序号规定
            for(int x = 10; x < orderBos.size(); x++) {
                LinkedHashMap<Integer, String> bo = (LinkedHashMap<Integer, String>) orderBos.get(x);
                /**具体配置如下             非抖音快手平台                                抖音快手平台
                 *     0                     "平台",                                       "平台",
                 *     1                     "序号",                                       "序号",
                 *     2                     "名称",                                       "名称",
                 *     3                     "账号ID",                                     "账号ID",
                 *     4                     "账号类型",                                   "账号类型",
                 *     5                     "资源形式",                                   "资源形式",
                 *     6                     "数量",                                       "数量",
                 *     7                     "档期范围开始时间",                            "档期范围开始时间",
                 *     8                      "档期范围结束时间"                            "档期范围结束时间"
                 *     9                     "@",                                         "@",
                 *     10                    "话题",                                        "话题"
                 *     11                    "电商链接",                                    "电商链接",
                 *     12                    "双微转发",                                    "双微转发",
                 *     13                    "电商肖像授权",                                "电商肖像授权",
                 *     14                    "信息流授权",                                  "信息流授权",
                 *     15                    "微任务（微博）",                             "星图/快接单",
                 *     16                    "报备（小红书）",                             "线下探店",
                 *     17                    "线下探店",                                    "其他特殊说明",(选填)
                 *     18                    "其他特殊说明",(选填)                           "产品提供方",
                 *     19                    "产品提供方",                                  "发布内容brief概述",
                 *     20                    "发布内容brief概述",                           "星图/快接单平台截图",
                 *     21                    "总价",                                        "截图时间",
                 *     22                    "佣金",                                        "星图/快接单平台报价（元）",
                 *     23                    "备注"                                         "折扣（%）",
                 *     24                                                                   "执行报价（元）",
                 *     25                                                                   "佣金",
                 *     26                                                                   "备注"
                 */
                //获取第一条数据的账号，如果等于示例数据则跳过
//                String accountName = bo.get(2);
//                if(name.equalsIgnoreCase(accountName)) {
//                    continue;
//                }
                //非空字段
                long count = bo.values().stream().filter(y -> y != null).count();
                //简单校验
                if("1".equals(req.getExcelType())) {
                    if(!("抖音".equals(bo.get(0)) || "快手".equals(bo.get(0)))) {
                        continue;
                    }
                    //抖音快手需要填写的字段20-1
                    if(bo.get(17) == null || bo.get(17) == "null" || bo.get(17).trim() == "") {
                        if(count < 19) {
                            continue;
                        }
                    } else {
                        if(count < 20) {
                            continue;
                        }
                    }
                } else {
                    if("抖音".equals(bo.get(0)) || "快手".equals(bo.get(0))) {
                        continue;
                    }
                    //非抖音快手需要填写的字段21-1
                    if(bo.get(18) == null || bo.get(18) == "null" || bo.get(18).trim() == "") {
                        if(count < 20) {
                            continue;
                        }
                    } else {
                        if(count < 21) {
                            continue;
                        }
                    }
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

                Map<String, String> map = new HashMap<>();
//                List<String>        title = excelTitle();
                for(int i = 0; i < fieldsBos.size(); i++) {
                    for(int j = 0; j < selfTitle.size(); j++) {
                        if(selfTitle.get(j).equalsIgnoreCase(fieldsBos.get(i).getTitle())) {
                            map.put(fieldsBos.get(i).getDataIndex(), bo.get(j));
                        }
                    }
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
        wrapper.orderByDesc(WorkOrder::getUtime);
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
        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.like(WorkOrder::getProjectName, req.getName());
        }
        if(Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime())) {
            wrapper.between(WorkOrder::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }
        wrapper.orderByDesc(WorkOrder::getUtime);
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

//    public List<String> excelTitle() {
//        return Arrays.asList("序号", "媒体", "账号", "账号ID或链接", "账号类型", "资源位置", "数量", "档期范围开始时间",
//                             "档期范围结束时间", "电商链接", "@", "话题", "电商肖像授权", "品牌双微转发授权", "微任务", "信息流授权", "报备",
//                             "星图/快接单", "线下探店", "其他", "产品提供方", "发布内容brief概述");
//    }


    /**
     * @param type 1是抖音快手模板，其他是非抖音快手模板
     * @return 模板表头
     */
    private List<String> excelTitle(String type) {
        if("1".equals(type)) {
            return Arrays.asList("平台", "序号", "名称", "账号ID", "账号类型", "资源形式", "数量", "档期范围开始时间", "档期范围结束时间", "@", "话题",
                                 "电商链接", "双微转发", "电商肖像授权", "信息流授权", "星图/快接单", "线下探店",
                                 "其他特殊说明", "产品提供方", "发布内容brief概述"
                    /* *//* 供应商填写*//*    , "星图/快接单平台截图", "截图时间", "星图/快接单平台报价（元）", "折扣（%）", "执行报价（元）", "佣金", "备注"*/);
        } else {
            return Arrays.asList("平台", "序号", "名称", "账号ID", "账号类型", "资源形式", "数量", "档期范围开始时间", "档期范围结束时间", "@", "话题",
                                 "电商链接", "双微转发", "电商肖像授权", "信息流授权", "微任务（微博）", "报备（小红书）", "线下探店",
                                 "其他特殊说明", "产品提供方", "发布内容brief概述"
                    /* 供应商填写*//*     ,"总价（元）", "佣金", "备注"*/);
        }
    }


}
