package cn.weihu.kol.userinfo;

import cn.weihu.kol.http.resp.PermissionResp;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {

    /**
     * 应用ID
     */
    private String               appid;
    /**
     * 应用认证
     */
    private String               appsecret;
    /**
     * 租户ID
     */
    private String               companyId;
    /**
     * 用户ID
     */
    private Long                 userId;
    /**
     * 用户名
     */
    private String               username;
    /**
     * 密码
     */
    private String               password;
    /**
     * 用户姓名
     */
    private String               name;
    /**
     * 远程调用认证
     */
    private String               token;
    /**
     * 页面调用认证
     */
    private String               auth;
    /**
     * token失效时间
     */
    private Long                 tokenExpireTime;
    /**
     * 权限列表
     */
    private List<PermissionResp> permissions;
    /**
     * 推送消息key
     */
    private String               msgKey;
    /**
     * 是否 超级管理员
     */
    private Boolean              isAdmin;
    private String               desKey;
    private List<String>         roleIds;

    public UserInfo() {
    }

    public UserInfo(String companyId, Long userId, String username, String password,
                    String name, List<PermissionResp> permissions) {
        this.companyId = companyId;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.permissions = permissions;
    }

    public UserInfo(String appid, String appsecret, String companyId) {
        this.appid = appid;
        this.appsecret = appsecret;
        this.companyId = companyId;
        this.isAdmin = false;
    }

}
