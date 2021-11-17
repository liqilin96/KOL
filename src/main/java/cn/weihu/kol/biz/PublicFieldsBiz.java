package cn.weihu.kol.biz;

import cn.weihu.kol.db.po.PublicFields;
import cn.weihu.kol.biz.Biz;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.req.PublicFieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;

import java.util.List;

/**
 * <p>
 * 字段字典表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-16
 */
public interface PublicFieldsBiz extends Biz<PublicFields> {

    String create(PublicFieldsReq req);

    String update(FieldsReq req);

    String delete(String id);

    List<FieldsResp> query(FieldsReq req);

    FieldsResp queryOne(String id);
}
