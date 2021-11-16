package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
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

    ResultBean ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response);

    void exportTemplate(HttpServletResponse response);

    /**
     * 列表
     *
     * @param req
     * @return
     */
    PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req);

    /**
     * 创建
     *
     * @param workOrder
     * @return
     */
    Long create(WorkOrder workOrder);
}
