package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.WorkOrderDataBiz;
import cn.weihu.kol.db.dao.WorkOrderDataDao;
import cn.weihu.kol.db.po.WorkOrderData;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工单数据表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class WorkOrderDataBizImpl extends ServiceImpl<WorkOrderDataDao, WorkOrderData> implements WorkOrderDataBiz {

}
