package cn.weihu.kol.userinfo;

public class UserInfoContext {

    private static final ThreadLocal<UserInfo> USER_CONTEXT = ThreadLocal.withInitial(UserInfo::new);
    public static final  String                KEY_LANG     = "lang";
    public static final  String                KEY_UUID     = "uuid";

    public UserInfoContext() {
    }

    public static String getCompanyId() {
        return ((UserInfo) USER_CONTEXT.get()).getCompanyId();
    }

    public static Long getUserId() {
        return ((UserInfo) USER_CONTEXT.get()).getUserId();
    }

    public static String getMsgKey() {
        return ((UserInfo) USER_CONTEXT.get()).getMsgKey();
    }

    public static String getFullName() {
        return ((UserInfo) USER_CONTEXT.get()).getName();
    }

    public static UserInfo getUserInfo() {
        return (UserInfo) USER_CONTEXT.get();
    }

    public static void setUserInfo(UserInfo userInfo) {
        USER_CONTEXT.set(userInfo);
    }

    public static void release() {
        USER_CONTEXT.remove();
    }
}
