package cn.weihu.kol.convert;

import cn.weihu.kol.db.po.User;
import cn.weihu.kol.http.req.UserSaveReq;
import cn.weihu.kol.http.resp.UserResp;

public class UserConverter {

    public static UserResp entity2UserResp(User user) {
        UserResp resp = new UserResp();
        resp.setId(user.getId().toString());
        resp.setName(user.getName());
        resp.setUsername(user.getUsername());
        resp.setPassword(user.getPassword());
        resp.setStatus(user.getStatus());
        resp.setCtime(user.getCtime());
        resp.setUtime(user.getUtime());
        resp.setCreateUserId(user.getCreateUserId());
        resp.setUpdateUserId(user.getUpdateUserId());
        return resp;
    }

    public static User userSaveReq2Entity(UserSaveReq req) {
        User user = new User();
        user.setName(req.getName());
        user.setStatus(0);
        user.setUsername(req.getUsername());
        return user;
    }
}
