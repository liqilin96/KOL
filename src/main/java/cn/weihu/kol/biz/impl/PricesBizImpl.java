package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.db.dao.PricesDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesLogsBoResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public PricesLogsBoResp starPrice(PricesLogsReq req) {


        //1 是资源库组
        Fields fields    = fieldsBiz.getById(1);
        String fieldList = fields.getFieldList();

        LambdaQueryWrapper<Prices> wrapper = new LambdaQueryWrapper<>();

        //达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.name\")) like {0}", "%" + req.getStarName() + "%");
        }

        //媒体平台
        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.platform\")) like {0}", "%" + req.getPlatform() + "%");
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
                wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.pricesForm\")) like {0}", "%" + priceForm + "%");
            }
        }

        List<PricesLogsResp> resps = new ArrayList<>();

        Prices prices = baseMapper.selectOne(wrapper);
        if(prices != null) {
            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(resp, prices);
            resps.add(resp);
        }
//        List<Prices> prices = baseMapper.selectList(wrapper);

        PricesLogsBoResp bo = new PricesLogsBoResp(resps, fieldList);
        return bo;
    }
}
