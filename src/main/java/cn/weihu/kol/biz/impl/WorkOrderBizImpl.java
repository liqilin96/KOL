package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.WorkOrderBiz;
import cn.weihu.kol.db.dao.WorkOrderDao;
import cn.weihu.kol.db.po.WorkOrder;
import cn.weihu.kol.http.req.WorkOrderReq;
import cn.weihu.kol.http.resp.WorkOrderResp;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public PageResult<WorkOrderResp> workOrderPage(WorkOrderReq req) {
        return null;
    }
}
