package cn.weihu.base.result;


import cn.weihu.base.exception.CheckException;
import org.springframework.context.MessageSource;

public class CheckUtil {
    private static MessageSource resources;

    public static void setResources(MessageSource resources) {
        CheckUtil.resources = resources;
    }

    public static void check(boolean condition, String msgKey, Object... args) {
        if(!condition) {
            fail(msgKey, args);
        }
    }

    public static void notEmpty(String str, String msgKey, Object... args) {
        if(str == null || str.isEmpty()) {
            fail(msgKey, args);
        }
    }

    public static void notNull(Object obj, String msgKey, Object... args) {
        if(obj == null) {
            fail(msgKey, args);
        }
    }

    public static void IntLimit(Integer obj, int min, int max, String msgKey, Object... args) {
        if(obj == null) {
            fail(msgKey + "不能为空", args);
        }
        if(obj.intValue() < min) {
            fail(msgKey + "不能小于" + min
                    , args);
        }
        if(obj.intValue() > max) {
            fail(msgKey + "不能大于" + max
                    , args);
        }
    }

    private static void fail(String msgKey, Object... args) {
        throw new CheckException(msgKey);
    }
}