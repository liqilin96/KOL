package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.kol.biz.QuoteBiz;
import cn.weihu.kol.db.dao.QuoteDao;
import cn.weihu.kol.db.po.Quote;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 达人报价表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-21
 */
@Slf4j
@Service
public class QuoteBizImpl extends BaseBiz<QuoteDao, Quote> implements QuoteBiz {

    @Override
    public Quote getOneByActorSn(Long projectId, String actorSn) {
        List<Quote> list = list(new LambdaQueryWrapper<>(Quote.class)
                                        .eq(Quote::getProjectId, projectId)
                                        .ge(Quote::getInsureEndtime, DateUtil.date())
                                        .eq(Quote::getEnableFlag, 1)
                                        .orderByDesc(Quote::getCtime));
        Quote quote = null;
        if(!CollectionUtils.isEmpty(list)) {
            quote = list.get(0);
        }
        return quote;
    }

    @Override
    public void updateBatchByActorSn(List<Quote> list) {
        LambdaUpdateWrapper<Quote> wrapper = Wrappers.lambdaUpdate(Quote.class);
        for(Quote quote : list) {
            wrapper.set(Quote::getEnableFlag, quote.getEnableFlag())
                    .set(Quote::getUtime, quote.getUtime())
                    .set(Quote::getUpdateUserId, quote.getUpdateUserId())
                    .eq(Quote::getActorSn, quote.getActorSn())
                    .eq(Quote::getProjectId, quote.getProjectId());
            update(wrapper);
            wrapper.clear();
        }
        log.info(">>> 报价表数据状态更新为未启用,size:{}", list.size());
    }
}
