package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.WorkOrderLogBiz;
import cn.weihu.kol.db.dao.WorkOrderLogDao;
import cn.weihu.kol.db.po.WorkOrderLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工单表记录表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-14
 */
@Service
public class WorkOrderLogBizImpl extends ServiceImpl<WorkOrderLogDao, WorkOrderLog> implements WorkOrderLogBiz {

}
