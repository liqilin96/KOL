package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.SystemLogService;
import cn.weihu.kol.db.dao.SystemLogMapper;
import cn.weihu.kol.db.po.SystemLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统的操作记录表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-09-16
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {

    @Override
    public void insertSystemLog(SystemLog log) {
        baseMapper.insert(log);
    }
}
