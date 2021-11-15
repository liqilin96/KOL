package cn.weihu.kol.biz;

import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface WorkOrderBiz extends IService<WorkOrder> {

    ResultBean<WorkOrderResp> ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response);
}
