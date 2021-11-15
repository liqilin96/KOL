package cn.weihu.kol.convert;

import cn.weihu.kol.db.po.Role;
import cn.weihu.kol.http.resp.RoleResp;

public class RoleConverter {

    public static RoleResp entity2RoleResp(Role role) {
        RoleResp resp = new RoleResp();
        resp.setId(role.getId().toString());
        resp.setName(role.getName());
        resp.setCtime(role.getCtime());
        resp.setUtime(role.getUtime());
        resp.setCreateUserId(role.getCreateUserId());
        resp.setUpdateUserId(role.getUpdateUserId());
        return resp;
    }
}
