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
//        //达人id
//        if(StringUtils.isNotBlank(req.getStarId())) {
//            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.IDorLink\")) like {0}", "%" + req.getStarId() + "%");
//        }
        //达人名称
        if(StringUtils.isNotBlank(req.getStarName())) {
            wrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(actor_data,\"$.account\")) like {0}", "%" + req.getStarName() + "%");
        }

        Page<PricesLogs> logsPage = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<PricesLogsResp> pricesLogsRespList = logsPage.getRecords().stream().map(x -> {

            PricesLogsResp resp = new PricesLogsResp();
            BeanUtils.copyProperties(x, resp);
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(logsPage.getTotal(),pricesLogsRespList,fieldList);
    }

}
