package cn.weihu.kol.biz.impl;

import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.PublicFields;
import cn.weihu.kol.db.dao.PublicFieldsDao;
import cn.weihu.kol.biz.PublicFieldsBiz;
import cn.weihu.kol.biz.impl.BaseBiz;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.req.PublicFieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段字典表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-16
 */
@Service
public class PublicFieldsBizImpl extends BaseBiz<PublicFieldsDao, PublicFields> implements PublicFieldsBiz {

    @Override
    public String create(PublicFieldsReq req) {

        PublicFields fields = new PublicFields();
        fields.setName(req.getName());
        fields.setFieldList(req.getFieldList());
        fields.setCtime(new Date());
        fields.setUtime(new Date());
        fields.setCreateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        this.save(fields);
        return fields.getId().toString();
    }

    @Override
    public String update(FieldsReq req) {
        PublicFields fields = new PublicFields();
        fields.setId(Long.parseLong(req.getId()));
        if(StringUtils.isNotBlank(req.getNewName())) {
            fields.setName(req.getNewName());
        }
        if(StringUtils.isNotBlank(req.getFieldList())) {
            fields.setFieldList(req.getFieldList());
        }
        fields.setUtime(new Date());
        fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        updateById(fields);
        return fields.getId().toString();
    }

    @Override
    public String delete(FieldsReq req) {
        this.baseMapper.deleteById(req.getId());
        return null;
    }

    @Override
    public List<FieldsResp> query(FieldsReq req) {
        LambdaQueryWrapper<PublicFields> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.eq(PublicFields::getName, req.getName());
        }
        wrapper.orderByDesc(PublicFields::getCtime);

        List<PublicFields> publicFields = this.baseMapper.selectList(wrapper);

        List<FieldsResp> fieldsResps = publicFields.stream().map(x -> {

            FieldsResp resp = new FieldsResp();
            resp.setId(x.getId().toString());
            resp.setName(x.getName());
            resp.setFieldList(x.getFieldList());
            return resp;
        }).collect(Collectors.toList());

        return fieldsResps;
    }
}
