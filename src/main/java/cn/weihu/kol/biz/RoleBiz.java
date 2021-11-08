package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Role;
import cn.weihu.kol.http.req.RoleListReq;
import cn.weihu.kol.http.req.RoleSaveReq;
import cn.weihu.kol.http.resp.RoleResp;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface RoleBiz extends Biz<Role> {

    PageResult<RoleResp> rolePage(RoleListReq req);

    List<RoleResp> roleAll();

    String roleAdd(RoleSaveReq req);

    String roleEdit(String id, RoleSaveReq req);

    String roleDel(String id);
}
