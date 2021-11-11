package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.db.dao.FieldsDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 字段组表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class FieldsBizImpl extends ServiceImpl<FieldsDao, Fields> implements FieldsBiz {

    @Override
    public String create(FieldsReq req) {

        Fields fields = new Fields();
        fields.setName(req.getName());
        fields.setType(Integer.parseInt(req.getType()));
        fields.setFieldList(req.getFieldList());
        fields.setCtime(LocalDateTime.now());
        fields.setUtime(LocalDateTime.now());
        fields.setCreateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
        this.save(fields);
        return null;
    }
}
