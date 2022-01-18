package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.QuoteBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.controller.ImportExcelStar;
import cn.weihu.kol.db.dao.QuoteDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Quote;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.req.QuoteReq;
import cn.weihu.kol.http.resp.QuoteResp;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private PricesBiz pricesBiz;

    @Override
    public Quote getOneByActorSn(Long projectId, String actorSn, String supplier) {
        List<Quote> list = list(new LambdaQueryWrapper<>(Quote.class)
                                        .eq(Quote::getProjectId, projectId)
                                        .eq(Quote::getActorSn, actorSn)
                                        .eq(Quote::getProvider, supplier)
                                        .ge(Quote::getInsureEndtime, DateUtil.date())
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
    public PageResult<QuoteResp> quotePage(QuoteReq req) {

        //4 是报价历史
        Fields fields    = fieldsBiz.getById(4);
        String fieldList = fields.getFieldList();

        LambdaQueryWrapper<Quote> wrapper = Wrappers.lambdaQuery(Quote.class);


        //达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getStarName() + "%");
        }

        // 媒体平台
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
        }
        wrapper.last("GROUP BY actor_sn ORDER BY count(1) DESC");

        Page<Quote> pricesPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<QuoteResp> respList = pricesPage.getRecords().stream().map(x -> {
            QuoteResp resp = new QuoteResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(pricesPage.getTotal(), respList, fieldList);
    }

//    @Override
//    public PageResult<QuoteResp> page(QuoteReq req) {
//        //4 是报价历史
//        Fields                    fields    = fieldsBiz.getById(4);
//        String                    fieldList = fields.getFieldList();
//        LambdaQueryWrapper<Quote> wrapper   = Wrappers.lambdaQuery(Quote.class);
//
//        // 达人名称
//        if(StringUtils.isNotBlank(req.getStarName())) {
//            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getStarName() + "%");
//        }
//        // 媒体平台
//        if(StringUtils.isNotBlank(req.getPlatform())) {
//            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
//        }
//        // 资源位置
//        if(StringUtils.isNotBlank(req.getPricesForm())) {
//            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.address\")) like {0}", "%" + req.getPricesForm() + "%");
//        }
//
//        Page<Quote> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
//        List<QuoteResp> quoteRespList = page.getRecords().stream().map(x -> {
//            QuoteResp resp = new QuoteResp();
//            BeanUtils.copyProperties(x, resp);
//            return resp;
//        }).collect(Collectors.toList());
//        return new PageResult<>(page.getTotal(), quoteRespList, fieldList);
//    }


    @Override
    public PageResult<QuoteResp> starPricePage(QuoteReq req) {
        //4 是报价历史
        Fields                    fields    = fieldsBiz.getById(4);
        String                    fieldList = fields.getFieldList();
        LambdaQueryWrapper<Quote> wrapper   = new LambdaQueryWrapper<>();
        //报价形式
        if(StringUtils.isNotBlank(req.getPricesForm())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.address\")) like {0}", "%" + req.getPricesForm() + "%");
        }

        //达人编号
        wrapper.eq(Quote::getActorSn, req.getActorSn());

        //创建时间
        if(req.getStartTime() != null && req.getEndTime() != null) {
            wrapper.between(Quote::getCtime, DateUtil.date(req.getStartTime()), DateUtil.date(req.getEndTime()));
        }

        Page<Quote> pricesPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<QuoteResp> respList = pricesPage.getRecords().stream().map(x -> {
            QuoteResp resp = new QuoteResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(pricesPage.getTotal(), respList, fieldList);
    }


    //    @Override
//    public void exportQuoteData(HttpServletResponse response, PricesLogsReq req) {
//        //4 是报价历史
//        Fields fields = fieldsBiz.getById(4);
//        //获取字段列表
//        List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
//        }.getType());
//
//        List<List<String>> exprotData = new ArrayList<>();
//
//        List<FieldsBo> newList = fieldsBos.stream().filter(x -> x.isEffect()).collect(Collectors.toList());
//        //获取中文表头
//        List<String> titleCN = newList.stream().map(FieldsBo::getTitle).collect(Collectors.toList());
//
//        exprotData.add(titleCN);
//        if(StringUtils.isBlank(req.getIds())) {
//            List<Quote> quotes  = this.list();
//            //导出所有数据
//            for(Quote quote : quotes) {
//                List<String>            data    = new ArrayList<>();
//                HashMap<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), HashMap.class);
//                pricesBiz.addExportData(exprotData, data, hashMap, newList);
//            }
//        } else {
//
//            String[] split = req.getIds().split(",");
//            for(int i = 0; i < split.length; i++) {
//                List<String>            data    = new ArrayList<>();
//                String                  id      = split[i];
//                Quote                  quote  = getById(id);
//                HashMap<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), HashMap.class);
//                pricesBiz.addExportData(exprotData, data, hashMap, newList);
//            }
//        }
//        try {
//            EasyExcelUtil.writeExcel(response, exprotData, "报价库");
//        } catch(
//                Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    @Override
    public void exportQuoteData(HttpServletResponse response, PricesLogsReq req) {
        //4 是报价历史
        Fields fields = fieldsBiz.getById(4);
        //获取字段列表
        List<FieldsBo> newList = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
        }.getType());

        List<List<String>> exprotData = new ArrayList<>();
        //获取中文表头
        List<String> titleCN = ImportExcelStar.kolTitle();
//        List<String> titleCN = newList.stream().map(FieldsBo::getTitle).collect(Collectors.toList());

        //添加额外多余表头
        List<String> list = ImportExcelStar.list2Title(newList, titleCN);
        titleCN.addAll(list);
        //按中文表头顺序转换
        List<FieldsBo> fieldsBos = ImportExcelStar.title2List(newList, titleCN);
        exprotData.add(titleCN);
        if(StringUtils.isBlank(req.getIds())) {
            List<Quote> quotes = this.list();
            //导出所有数据
            for(Quote quote : quotes) {
                List<String>            data    = new ArrayList<>();
//                HashMap<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), HashMap.class);
                Map<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), new TypeToken<Map<String, String>>() {
                }.getType());
                pricesBiz.addExportData(exprotData, data, hashMap, fieldsBos);
            }
        } else {

            String[] split = req.getIds().split(",");
            for(int i = 0; i < split.length; i++) {
                List<String>            data    = new ArrayList<>();
                String                  id      = split[i];
                Quote                   quote   = getById(id);
//                HashMap<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), HashMap.class);
                Map<String, String> hashMap = GsonUtils.gson.fromJson(quote.getActorData(), new TypeToken<Map<String, String>>() {
                }.getType());
                pricesBiz.addExportData(exprotData, data, hashMap, fieldsBos);
            }
        }
        try {
            EasyExcelUtil.writeExcel(response, exprotData, "报价库");
        } catch(
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void batchSaveOrUpdate(List<Quote> list) {
        if(!CollectionUtils.isEmpty(list)) {
            List<Quote> insertList = new ArrayList<>();
            List<Quote> updateList = new ArrayList<>();
            for(Quote quote : list) {
                //
                Quote oneByActorSn = getOne(new LambdaQueryWrapper<>(Quote.class)
                                                    .eq(Quote::getProjectId, quote.getProjectId())
                                                    .eq(Quote::getActorSn, quote.getActorSn())
                                                    .eq(Quote::getProvider, quote.getProvider()));
                if(null != oneByActorSn) {
                    oneByActorSn.setActorData(quote.getActorData());
                    oneByActorSn.setCommission(quote.getCommission());
                    oneByActorSn.setPrice(quote.getPrice());
                    oneByActorSn.setProvider(quote.getProvider());
                    oneByActorSn.setInsureEndtime(quote.getInsureEndtime());
                    oneByActorSn.setUtime(DateUtil.date());
                    updateList.add(oneByActorSn);
                } else {
                    oneByActorSn = new Quote();
                    oneByActorSn.setProjectId(quote.getProjectId());
                    oneByActorSn.setActorSn(quote.getActorSn());
                    oneByActorSn.setActorData(quote.getActorData());
                    oneByActorSn.setCommission(quote.getCommission());
                    oneByActorSn.setPrice(quote.getPrice());
                    oneByActorSn.setProvider(quote.getProvider());
                    oneByActorSn.setInsureEndtime(quote.getInsureEndtime());
                    oneByActorSn.setEnableFlag(1);
                    oneByActorSn.setCtime(DateUtil.date());
                    oneByActorSn.setUtime(DateUtil.date());
                    insertList.add(quote);
                }
            }
            if(!CollectionUtils.isEmpty(insertList)) {
                saveBatch(insertList,insertList.size());
                log.info(">>> 报价库插入数据完成...size:{}", insertList.size());
            }
            if(!CollectionUtils.isEmpty(updateList)) {
                updateBatchById(updateList,insertList.size());
                log.info(">>> 报价库更新数据完成...size:{}", updateList.size());
            }
        }
    }


}
