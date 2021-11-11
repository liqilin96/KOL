package cn.weihu.kol.biz;

import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.http.req.FieldsReq;
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

    String create(FieldsReq req);

}
