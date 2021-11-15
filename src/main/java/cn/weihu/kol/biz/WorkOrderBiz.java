package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface WorkOrderBiz extends IService<WorkOrder> {

    PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req);
}
