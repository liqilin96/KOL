package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.QuoteBiz;
import cn.weihu.kol.db.dao.QuoteDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Quote;
import cn.weihu.kol.http.req.QuoteReq;
import cn.weihu.kol.http.resp.QuoteResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private FieldsBiz fieldsBiz;

    @Override
    public Quote getOneByActorSn(Long projectId, String actorSn) {
        List<Quote> list = list(new LambdaQueryWrapper<>(Quote.class)
                                        .eq(Quote::getProjectId, projectId)
                                        .eq(Quote::getActorSn, actorSn)
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

    @Override
    public PageResult<QuoteResp> page(QuoteReq req) {
        //4 是报价历史
        Fields                    fields    = fieldsBiz.getById(4);
        String                    fieldList = fields.getFieldList();
        LambdaQueryWrapper<Quote> wrapper   = Wrappers.lambdaQuery(Quote.class);

        // 达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getStarName() + "%");
        }
        // 媒体平台
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
        }
        // 资源位置
        if(StringUtils.isNotBlank(req.getPricesForm())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.address\")) like {0}", "%" + req.getPricesForm() + "%");
        }

        Page<Quote> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<QuoteResp> quoteRespList = page.getRecords().stream().map(x -> {
            QuoteResp resp = new QuoteResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), quoteRespList, fieldList);
    }

    @Override
    public void batchSaveOrUpdate(List<Quote> list) {
        if(!CollectionUtils.isEmpty(list)) {
            baseMapper.batchSaveOrUpdate(list);
        }
    }
}
