package cn.weihu.kol.biz;

import cn.weihu.kol.http.resp.PermissionResp;
import cn.weihu.kol.db.po.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface PermissionBiz extends Biz<Permission> {

    List<PermissionResp> permissionList();
}
