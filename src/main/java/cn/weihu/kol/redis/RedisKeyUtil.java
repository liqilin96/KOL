package cn.weihu.kol.redis;

import org.apache.commons.lang3.StringUtils;

/**
 * redisKey设计
 */
public class RedisKeyUtil {

    public static String getKey(Object... tableName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("kol:");
        String s = StringUtils.join(tableName, ":");
        buffer.append(s);
        return buffer.toString();
    }

    private static String getKey_company(String companyId, String type) {
        return getKey("company", companyId, type);
    }

    /**
     * 企业配置
     *
     * @param companyId
     * @return
     */
    static String getKey_company_config(String companyId) {
        return getKey_company(companyId, "config");
    }

    /**
     * 企业列表
     *
     * @param type
     * @return
     */
    private static String getKey_company_list(String type) {
        return getKey("company:list", type);
    }

    static String getKey_company_list_run() {
        return getKey_company_list("run_set");
    }
}