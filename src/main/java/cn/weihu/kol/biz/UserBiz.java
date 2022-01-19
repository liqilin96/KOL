package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.User;
import cn.weihu.kol.http.req.*;
import cn.weihu.kol.http.resp.LoginResp;
import cn.weihu.kol.http.resp.UserResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface UserBiz extends Biz<User> {

    PageResult<UserResp> userPage(UserListReq req);

    String addUser(UserSaveReq req);

    String editUser(String id, UserSaveReq req);

    String resetPassword(String id);

    LoginResp login(LoginReq req,HttpServletRequest request);

    String modifyPassword(ModifyPasswordReq req);

    List<UserResp> userList();

    Map<String, Object> token(String accessKeyId, String accessKeySecret);

    void delToken(HttpServletRequest request);

    List<UserResp> mediums();

    String receiveMediums(MediumReq req);
}
