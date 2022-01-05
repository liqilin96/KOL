package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
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

    String ImportData(MultipartFile file, WorkOrderReq req, HttpServletResponse response);

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
     * @param type
     * @param status
     * @return
     */
    WorkOrder create(WorkOrder workOrder, Integer type, String status);

    /**
     * 待报价列表
     *
     * @param req
     * @return
     */
    PageResult<WorkOrderResp> waitWorkOrderPage(WorkOrderReq req);

    String importPicture(MultipartFile file);

    void downloadPicTure(String picturePath, HttpServletResponse response);

    String uploadPDF(MultipartFile file,String workOrderId);
}
