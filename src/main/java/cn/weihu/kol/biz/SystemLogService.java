package cn.weihu.kol.biz;

import cn.weihu.kol.db.po.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统的操作记录表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-09-16
 */
public interface SystemLogService extends IService<SystemLog> {

    void insertSystemLog(SystemLog log);

}
