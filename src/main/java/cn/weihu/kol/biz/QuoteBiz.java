package cn.weihu.kol.biz;

import cn.weihu.kol.db.po.Quote;

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

    Quote getOneByActorSn(Long projectId, String actorSn);

    void updateBatchByActorSn(List<Quote> list);
}
