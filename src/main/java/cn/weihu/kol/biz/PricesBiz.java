package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesDetailsResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * <p>
 * 达人报价表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
public interface PricesBiz extends IService<Prices> {

    PageResult<PricesLogsResp> starPage(PricesLogsReq req);

    PageResult<PricesLogsResp> starPricePage(PricesLogsReq req);

    Prices getOneByActorSn(String actorSn);

    PricesDetailsResp starDetail(PricesLogsReq req);

    void exportStarData(HttpServletResponse response,PricesLogsReq req);

    Set<String> starTab(String tab);

    PageResult<PricesLogsResp> expirtPrices(PricesLogsReq req);

    void expirtPricesExport(PricesLogsReq req,HttpServletResponse response);

    void savePrices(Prices prices) ;
}
