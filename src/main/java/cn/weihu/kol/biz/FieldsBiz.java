package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字段组表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface FieldsBiz extends IService<Fields> {

    String createGroup(FieldsReq req);

    String deleteGroup(String id);

    String updateGroup(FieldsReq req);

    PageResult<FieldsResp> pageGroup(FieldsReq req);

    FieldsResp queryOne(String id);

//    String create(FieldsReq req);

//    String updateField(FieldsReq req);

    Fields getOneByType(Integer type);
}
