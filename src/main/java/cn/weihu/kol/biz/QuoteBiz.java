package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Quote;
import cn.weihu.kol.http.req.QuoteReq;
import cn.weihu.kol.http.resp.QuoteResp;

import java.util.List;

/**
 * <p>
 * 达人报价表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-21
 */
public interface QuoteBiz extends Biz<Quote> {

    Quote getOneByActorSn(Long projectId, String actorSn, String supplier);

    void updateBatchByActorSn(List<Quote> list);

//    PageResult<QuoteResp> page(QuoteReq req);

    void batchSaveOrUpdate(List<Quote> list);

    PageResult<QuoteResp> quotePage(QuoteReq req);

    PageResult<QuoteResp> starPricePage(QuoteReq req);
}
