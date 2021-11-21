package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.db.dao.PricesDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesDetailsResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.GsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 达人报价表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@Service
public class PricesBizImpl extends ServiceImpl<PricesDao, Prices> implements PricesBiz {


    @Autowired
    private FieldsBiz fieldsBiz;

    @Override
    public PageResult<PricesLogsResp> starPage(PricesLogsReq req) {

        //1 是资源库组
        Fields fields    = fieldsBiz.getById(1);
        String fieldList = fields.getFieldList();

        LambdaQueryWrapper<Prices> wrapper = new LambdaQueryWrapper<>();

        //媒体平台
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
        }
        //账号类型
        if(StringUtils.isNotBlank(req.getAccountType())) {
            String[] split = req.getAccountType().split(",");
            for(int i = 0; i < split.length; i++) {
                String type = split[i];
                wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.accountType\")) like {0}", "%" + type + "%");
            }
        }
        //报价形式
        if(StringUtils.isNotBlank(req.getPricesForm())) {
            String[] split = req.getPricesForm().split(",");
            for(int i = 0; i < split.length; i++) {
                String priceForm = split[i];
                wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.priceType\")) like {0}", "%" + priceForm + "%");
            }
        }
        //达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getStarName() + "%");
            wrapper.last("GROUP BY JSON_UNQUOTE(JSON_EXTRACT(actor_data, \"$.account\")) like \"%" + req.getStarName() + "%\"");
        } else {
            wrapper.last("GROUP BY JSON_UNQUOTE(JSON_EXTRACT(actor_data, \"$.account\"))");
        }
        Page<Prices> pricesPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<PricesLogsResp> respList = pricesPage.getRecords().stream().map(x -> {
            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(pricesPage.getTotal(), respList, fieldList);
    }

    @Override
    public PageResult<PricesLogsResp> starPricePage(PricesLogsReq req) {
        //4 是报价历史
        Fields                     fields    = fieldsBiz.getById(4);
        String                     fieldList = fields.getFieldList();
        LambdaQueryWrapper<Prices> wrapper   = new LambdaQueryWrapper<>();


        //媒体平台
        wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
        //达人名称
        wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", req.getStarName());
        Page<Prices> pricesPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<PricesLogsResp> respList = pricesPage.getRecords().stream().map(x -> {
            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(pricesPage.getTotal(), respList, fieldList);
    }

    @Override
    public Prices getOneByActorSn(String actorSn) {
        return getOne(new LambdaQueryWrapper<>(Prices.class).eq(Prices::getActorSn, actorSn));
    }

    @Override
    public PricesDetailsResp starDetail(PricesLogsReq req) {
        PageResult<PricesLogsResp> pageResult = starPricePage(req);
        List<PricesLogsResp>       resps      = pageResult.getRecords();
        Map<String, String>        map        = new HashMap<>();

        long sum = resps.stream().filter(x -> x.getInsureEndtime().after(new Date())).map(x -> {

            HashMap<String, String> hashMap = GsonUtils.gson.fromJson(x.getActorData(), HashMap.class);
            return map.put(hashMap.get("address"), x.getPrice().toString());
        }).count();
        return new PricesDetailsResp(sum, map);
    }

    @Override
    public void exportStarData(HttpServletResponse response, PricesLogsReq req) {
        //4 是报价历史
        Fields fields = fieldsBiz.getById(4);
        //获取字段列表
        List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
        }.getType());

        List<List<String>> exprotData = new ArrayList<>();

        List<FieldsBo> newList = fieldsBos.stream().filter(x -> x.isEffect()).collect(Collectors.toList());
        //获取中文表头
        List<String> titleCN = newList.stream().map(FieldsBo::getTitle).collect(Collectors.toList());

        exprotData.add(titleCN);
        if(StringUtils.isBlank(req.getIds())) {
            List<Prices> pricesList = this.list();
            //导出所有数据
            for(Prices prices : pricesList) {
                List<String> data = new ArrayList<>();
                addExportData(exprotData, data, prices, newList);
            }
        } else {

            String[] split = req.getIds().split(",");
            for(int i = 0; i < split.length; i++) {
                List<String> data   = new ArrayList<>();
                String       id     = split[i];
                Prices       prices = getById(id);
                addExportData(exprotData, data, prices, newList);
            }
        }
        try {
            EasyExcelUtil.writeExcel(response, exprotData, "数据详情");
        } catch(
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Set<String> starTab(String tab) {
        //账号类型
        LambdaQueryWrapper<Prices> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.accountType\")) like {0}", "%" + tab + "%");

        List<Prices> prices  = this.baseMapper.selectList(wrapper);
        Set<String>  tabList = new HashSet<>();
        for(Prices price : prices) {
            HashMap hashMap = GsonUtils.gson.fromJson(price.getActorData(), HashMap.class);
            if(hashMap.get("accountType") != null) {
                String[] accountTypes = hashMap.get("accountType").toString().split(",");
                for(int i = 0; i < accountTypes.length; i++) {
                    String accountType = accountTypes[i];
                    if(accountType.contains(tab)) {
                        tabList.add(accountType);
                    }
                }
            }
        }
        return tabList;
    }


    @Override
    public PageResult<PricesLogsResp> expirtPrices(PricesLogsReq req) {

        LambdaQueryWrapper<Prices> wrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getPlatform() + "%");

        }
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.media\")) like {0}", "%" + req.getPlatform() + "%");
        }
        wrapper.between(Prices::getInsureEndtime, new Date(), expirtDate(new Date()));
        Page<Prices> pricesPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<PricesLogsResp> respList = pricesPage.getRecords().stream().map(x -> {
            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());

        return new PageResult<>(pricesPage.getTotal(), respList);
    }

    @Override
    public void expirtPricesExport(PricesLogsReq req, HttpServletResponse response) {

        //4 是报价历史
        Fields fields = fieldsBiz.getById(4);
        //获取字段列表
        List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
        }.getType());

        List<List<String>> exprotData = new ArrayList<>();

        List<FieldsBo> newList = fieldsBos.stream().filter(x -> x.isEffect()).collect(Collectors.toList());
        //获取中文表头
        List<String> titleCN = newList.stream().map(FieldsBo::getTitle).collect(Collectors.toList());

        exprotData.add(titleCN);
        if(StringUtils.isBlank(req.getIds())) {
            List<PricesLogsResp> records = this.expirtPrices(req).getRecords();
            List<Prices> pricesList = records.stream().map(x -> {
                Prices prices = new Prices();
                BeanUtils.copyProperties(x, prices);
                return prices;
            }).collect(Collectors.toList());
            //导出所有数据
            for(Prices prices : pricesList) {
                List<String> data = new ArrayList<>();
                addExportData(exprotData, data, prices, newList);
            }
        } else {

            String[] split = req.getIds().split(",");
            for(int i = 0; i < split.length; i++) {
                List<String> data   = new ArrayList<>();
                String       id     = split[i];
                Prices       prices = getById(id);
                addExportData(exprotData, data, prices, newList);
            }
        }
        try {
            EasyExcelUtil.writeExcel(response, exprotData, "保价即将到期");
        } catch(
                Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @param exprotData 导出的数据
     * @param data       每一条数据
     * @param prices     单个达人报价
     * @param newList    导出的头
     */
    public void addExportData(List<List<String>> exprotData, List<String> data, Prices prices, List<FieldsBo> newList) {

        String                  actorData = prices.getActorData();
        HashMap<String, String> hashMap   = GsonUtils.gson.fromJson(actorData, HashMap.class);

        for(int j = 0; j < newList.size(); j++) {
            boolean flag = true;
            for(String key : hashMap.keySet()) {
                String dataIndex = newList.get(j).getDataIndex();
                if(key.equalsIgnoreCase(dataIndex)) {
                    data.add(hashMap.get(key));
                    flag = false;
                    break;
                }
            }
            if(flag) {
                data.add("");
            }
        }
        exprotData.add(data);
    }


    //返回15天的日子
    private Date expirtDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 15);
        System.out.println("---------------------------------------------");
        System.out.println(c.getTime() + "============" + new Date());
        System.out.println("---------------------------------------------");
        return c.getTime();
    }

}
