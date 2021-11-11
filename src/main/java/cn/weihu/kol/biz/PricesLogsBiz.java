package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.PricesLogs;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesLogsBoResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报价记录表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
public interface PricesLogsBiz extends IService<PricesLogs> {

    PageResult<PricesLogsBoResp> pages(PricesLogsReq req);
}
