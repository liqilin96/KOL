package cn.weihu.kol.redis;

import cn.weihu.kol.userinfo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cn.weihu.kol.util.GsonUtils.gson;

@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private EnhancedRedisService redisService;

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

}