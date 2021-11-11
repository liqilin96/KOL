package cn.weihu.kol.redis;

import cn.weihu.kol.runner.StartupRunner;
import cn.weihu.kol.userinfo.UserInfo;
import cn.weihu.kol.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static cn.weihu.kol.util.GsonUtils.gson;

@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private EnhancedRedisService redisService;
    @Autowired
    @Qualifier("redisTemplateA")
    private StringRedisTemplate  redisTemplate;

    public void setUserInfoByUsername(String username, UserInfo userInfo) {
        String key = RedisKeyUtil.getKey("userInfo", "username", username);
        redisService.set(key, gson.toJson(userInfo), 12L, TimeUnit.HOURS);
    }

    public UserInfo getUserInfoByUsername(String username) {
        String key  = RedisKeyUtil.getKey("userInfo", "username", username);
        String json = redisService.get(key);
        return json == null ? null : gson.fromJson(json, UserInfo.class);
    }

    public void delUserInfoByUsername(String username) {
        String key = RedisKeyUtil.getKey("userInfo", "username", username);
        redisService.del(key);
    }

    public void setUserInfoByAppid(String appid, UserInfo userInfo) {
        String key = RedisKeyUtil.getKey("userInfo", "appid", appid);
        redisService.set(key, userInfo);
    }

    public void setUserInfoByAppidNx(String appid, UserInfo userInfo) {
        String key = RedisKeyUtil.getKey("userInfo", "appid", appid);
        redisTemplate.opsForValue().setIfAbsent(key, gson.toJson(userInfo));
    }

    public UserInfo getUserInfoByAppid(String appid) {
        String key = RedisKeyUtil.getKey("userInfo", "appid", appid);
        Object obj = redisService.get(key);
        return obj == null ? null : gson.fromJson(obj.toString(), UserInfo.class);
    }

    public void setUserInfoByToken(String token, UserInfo userInfo) {
        String key = RedisKeyUtil.getKey("userInfo", "token", token);
        redisService.set(key, gson.toJson(userInfo), 12L, TimeUnit.HOURS);
    }

    public UserInfo getUserInfoByToken(String token) {
        String key  = RedisKeyUtil.getKey("userInfo", "token", token);
        String json = redisService.get(key);
        return json == null ? null : gson.fromJson(json, UserInfo.class);
    }

    public void delUserInfoByToken(String token) {
        String key = RedisKeyUtil.getKey("userInfo", "token", token);
        redisService.del(key);
    }

    public void setExpireOfUserInfoByToken(String token, long time, TimeUnit unit) {
        String key = RedisKeyUtil.getKey("userInfo", "token", token);
        redisService.expire(key, time, unit);
    }


    public String blpop(String key) {
        //list集合 第一个元素为key值,第二个元素为弹出的元素值;当超时返回[null]
        return redisService.lPop(key);
    }
}