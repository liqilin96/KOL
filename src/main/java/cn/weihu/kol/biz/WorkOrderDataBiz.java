package cn.weihu.kol.biz;

import cn.weihu.kol.db.po.WorkOrderData;
import cn.weihu.kol.http.req.WorkOrderBatchUpdateReq;
import cn.weihu.kol.http.req.WorkOrderDataOrderReq;
import cn.weihu.kol.http.req.WorkOrderDataReq;
import cn.weihu.kol.http.req.WorkOrderDataReviewReq;
import cn.weihu.kol.http.resp.WorkOrderDataCompareResp;
import cn.weihu.kol.http.resp.WorkOrderDataResp;
import cn.weihu.kol.http.resp.WorkOrderDataScreeningResp;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 工单数据表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface WorkOrderDataBiz extends IService<WorkOrderData> {

    List<WorkOrderDataResp> workOrderDataList(WorkOrderDataReq req);

    List<WorkOrderDataResp> waitWorkOrderDataList(WorkOrderDataReq req);

    Long updateWorkOrderData(WorkOrderBatchUpdateReq req);

    WorkOrderDataScreeningResp screening(WorkOrderBatchUpdateReq req);

    Long enquiry(WorkOrderBatchUpdateReq req);

    Long enquiryAgain(WorkOrderBatchUpdateReq req);

    Long quote(WorkOrderBatchUpdateReq req);

    WorkOrderDataCompareResp quoteList(WorkOrderDataReq req);

    Long order(WorkOrderDataOrderReq req);

    Long review(WorkOrderDataReviewReq req);

    void detailExport(WorkOrderBatchUpdateReq req, HttpServletResponse response);

    void supplierExport(WorkOrderBatchUpdateReq req, HttpServletResponse response);

    void quoteListExport(WorkOrderBatchUpdateReq req, HttpServletResponse response);

    void workOrderDataListExport(WorkOrderDataReq req, HttpServletResponse response);

    String delete(String workOrderIds);

    String lostPromise(String workOrderDataId,double price);

    List<WorkOrderDataResp> lostPromiseList(String workOrderId);

    String remake(String workOrderDataId, double price);
}
