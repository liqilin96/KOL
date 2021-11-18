package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.db.dao.PricesDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesLogsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.plat\")) like {0}", "%" + req.getPlatform() + "%");
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
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.talent\")) like {0}", "%" + req.getStarName() + "%");
            wrapper.last("GROUP BY JSON_UNQUOTE(JSON_EXTRACT(actor_data, \"$.talent\")) like \"%" + req.getStarName() + "%\"");
        } else {
            wrapper.last("GROUP BY JSON_UNQUOTE(JSON_EXTRACT(actor_data, \"$.talent\"))");
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
        //1 是资源库组
        Fields fields    = fieldsBiz.getById(1);
        String fieldList = fields.getFieldList();

        LambdaQueryWrapper<Prices> wrapper = new LambdaQueryWrapper<>();

        //达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.talent\")) like {0}", "%" + req.getStarName() + "%");
        }

        //媒体平台
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.plat\")) like {0}", "%" + req.getPlatform() + "%");
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
}
