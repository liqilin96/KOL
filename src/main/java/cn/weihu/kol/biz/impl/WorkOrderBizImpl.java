package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import cn.weihu.kol.util.ExcelExport;
import cn.weihu.kol.util.ExcelUtil;
import cn.weihu.kol.util.ExceptionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        WorkOrderResp resp = new WorkOrderResp();

        try {
            //校验文件类型
            if (!file.getOriginalFilename().endsWith("xls") && !file.getOriginalFilename().endsWith("xlsx")) {
                log.error(file.getOriginalFilename() + "不是excel文件");
                throw new IOException(file.getOriginalFilename() + "不是excel文件");
            }

            List<String> data = ExcelUtil.readLines("Excel", file.getInputStream());
            //读取到的表头
            String excel = data.get(0);


//            List<Map<String, Object>> list = ExcelExport.ExcelReadMap(file, englishHeader);


        }catch(Exception e) {
            resultBean.setData(null);
            resultBean.setCode(ResultBean.FAIL);
            resultBean.setMsg(ExceptionUtil.getMessage(e));
            return resultBean;
        }


        return null;
    }
    @Override
    public PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req) {
        return null;
    }
}
