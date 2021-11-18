package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesLogsBiz;
import cn.weihu.kol.db.dao.PricesLogsDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.PricesLogs;
import cn.weihu.kol.http.req.PricesLogsReq;
import cn.weihu.kol.http.resp.PricesLogsBoResp;
import cn.weihu.kol.http.resp.PricesLogsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报价记录表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@Service
public class PricesLogsBizImpl extends ServiceImpl<PricesLogsDao, PricesLogs> implements PricesLogsBiz {


    @Autowired
    private FieldsBiz fieldsBiz;


    @Override
    public PageResult<PricesLogsResp> pages(PricesLogsReq req) {

        //4 是报价历史
        Fields fields    = fieldsBiz.getById(4);
        String fieldList = fields.getFieldList();


        LambdaQueryWrapper<PricesLogs> wrapper = new LambdaQueryWrapper<>();

        //SELECT * from obc_customer_header c where JSON_UNQUOTE(JSON_EXTRACT(c.params,"$.name"))='李四'
//        wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.name\")) like {0}", "%四%");
//        wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.age\")) > {0}", "25");

        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.name\")) like {0}", "%" + req.getStarName() + "%");
        }

        if(StringUtils.isNotBlank(req.getPlatform())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.platform\")) like {0}", "%" + req.getPlatform() + "%");
        }
        //TODO 条件多了再说！！！！

        Page<PricesLogs> logsPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<PricesLogsResp> pricesLogsRespList = logsPage.getRecords().stream().map(x -> {

            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(resp, x);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(logsPage.getTotal(),pricesLogsRespList,fieldList);
    }

}
