//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.weihu.kol.redis;

import cn.weihu.kol.util.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class EnhancedRedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private RedisScript<Long>   lPushIfAbsent            = new DefaultRedisScript("local list = redis.call(\"lrange\", KEYS[1], 0, -1);\nfor k, v in pairs(list) do\n  if v == ARGV[1] then\n    return 0;\n  end\nend\nreturn redis.call(\"lpush\", KEYS[1], ARGV[1]);", Long.class);
    private RedisScript<Long>   lPushSorted              = new DefaultRedisScript("local list = redis.call(\"lrange\", KEYS[1], 0, -1);\nfor k, v in pairs(list) do\n  if v == ARGV[1] then\n    return 0;\n  elseif tonumber(v) > tonumber(ARGV[1]) then\n    return redis.call(\"linsert\", KEYS[1], \"before\", v, ARGV[1]);\n  end\nend\nreturn redis.call(\"rpush\", KEYS[1], ARGV[1]);", Long.class);
    private RedisScript<String> rPopIfNotEmpty           = new DefaultRedisScript("while true do\n  local value = redis.call(\"rpop\", KEYS[1]);\n  if not value then\n    return nil;\n  elseif redis.call(\"llen\", KEYS[2] .. \":\" .. value .. ARGV[1]) > 0 then\n    redis.call(ARGV[2], KEYS[1], value);\n    return value;\n  end\nend", String.class);
    private RedisScript<List>   zRangeRemByScore         = new DefaultRedisScript("local zset = redis.call(\"zrangebyscore\", KEYS[1], ARGV[1], ARGV[2]);\nfor k, v in pairs(zset) do\n  redis.call(\"zrem\", KEYS[1], v);\nend\nreturn zset;", List.class);
    private RedisScript<List>   zRangeRemByScoreAndLimit = new DefaultRedisScript("local zset = redis.call(\"zrangebyscore\", KEYS[1], ARGV[1], ARGV[2], \"limit\", ARGV[3], ARGV[4]);\nfor k, v in pairs(zset) do\n  redis.call(\"zrem\", KEYS[1], v);\nend\nreturn zset;", List.class);
    private RedisScript<Long>   zAddIfAbsent             = new DefaultRedisScript("local value = redis.call(\"zscore\", KEYS[1], ARGV[1]);\nif not value then\n  return redis.call(\"zadd\", KEYS[1], ARGV[2], ARGV[1]);\nend\nreturn 0;", Long.class);


    protected boolean doTryLock(String key, String value, long expireTime, TimeUnit expireTimeUnit) {
        return (Long) this.redisTemplate.execute(new DefaultRedisScript("if (redis.call(\"set\", KEYS[1], ARGV[1], \"NX\", \"PX\", ARGV[2])) then return 1 else return 0 end", Long.class), Arrays.asList(key), new Object[]{value, Long.toString(expireTimeUnit.toMillis(expireTime))}) == 1L;
    }

    protected void doUnlock(String key, String value) {
        this.redisTemplate.execute(new DefaultRedisScript("if redis.call(\"get\",KEYS[1]) == ARGV[1]\nthen\n    return redis.call(\"del\",KEYS[1])\nelse\n    return 0\nend", Boolean.class), Arrays.asList(key), new Object[]{value});
    }

    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return this.redisTemplate.expire(key, timeout, unit);
    }

    public void del(final String key) {
        this.redisTemplate.delete(key);
    }

    public void del(List<String> keys) {
        this.redisTemplate.delete(keys);
    }

    public Set<String> keys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    public void set(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value) {
        String val;
        if(value instanceof String) {
            val = (String) value;
        } else {
            val = GsonUtils.gson.toJson(value);
        }

        this.redisTemplate.opsForValue().set(key, val);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return (String) this.redisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clz) {
        return GsonUtils.gson.fromJson((String) this.redisTemplate.opsForValue().get(key), clz);
    }

    public Long increment(String key, long num) {
        return this.redisTemplate.opsForValue().increment(key, num);
    }

    public Long increment(String key, String hashKey, long delta) {
        return this.redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public void hPut(String key, String field, String value) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.put(key, field, value);
    }


    public void hPutIfAbsent(String key, String field, String value) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.putIfAbsent(key, field, value);
    }

    public boolean hExists(String key, String field) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.hasKey(key, field);
    }

    public void hSet(String key, String field, String value) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.put(key, field, value);
    }

    public String hGet(String key, String field) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return (String) hashOperations.get(key, field);
    }

    public Map<String, String> hGetAll(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void hDel(String key, String field) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.delete(key, new Object[]{field});
    }

    public Long hDel(String key, Object... fields) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.delete(key, fields);
    }

    public Long hLen(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.size(key);
    }

    public boolean sIsMember(String key, String member) {
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        return setOperations.isMember(key, member);
    }

    public Set<String> hKeys(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.keys(key);
    }

    public List<String> hVals(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.values(key);
    }

    public Long sAdd(String key, List<String> member) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(member);
        return this.sAdd(key, (String[]) member.toArray(new String[0]));
    }

    public Long sAdd(String key, String... member) {
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        return setOperations.add(key, member);
    }

    public Long sSize(String key) {
        SetOperations<String, String> stringStringSetOperations = this.redisTemplate.opsForSet();
        return stringStringSetOperations.size(key);
    }

    public Long sDel(String key, String member) {
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        return setOperations.remove(key, new Object[]{member});
    }

    public Set<String> sMembers(String key) {
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    public Long lPush(String key, String value) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.leftPush(key, value);
    }

    public Long lPush(String key, Object value) {
        String val;
        if(value instanceof String) {
            val = (String) value;
        } else {
            val = GsonUtils.gson.toJson(value);
        }

        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.leftPush(key, val);
    }

    public Long lPush(String key, List<String> values) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.leftPushAll(key, values);
    }

    public Long rPush(String key, String value) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.rightPush(key, value);
    }

    public Long rPush(String key, List<String> values) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.rightPushAll(key, values);
    }

    public String lPop(String key) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return (String) listOperations.leftPop(key);
    }

    public List<String> lRange(String key, int start, int stop) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.range(key, (long) start, (long) stop);
    }

    public Long lLen(String key) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.size(key);
    }

    public String rPop(String key) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return (String) listOperations.rightPop(key);
    }

    public String rPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return (String) listOperations.rightPop(key, timeout, unit);
    }

    public String rPopLPush(String source, String target) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return (String) listOperations.rightPopAndLeftPush(source, target);
    }

    public String brpop(String key, long timeout, TimeUnit unit) {
        return (String) this.redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    public Long lRem(String key, String value) {
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.remove(key, 0L, value);
    }

    public Long lPushIfAbsent(String key, String value) {
        return (Long) this.redisTemplate.execute(this.lPushIfAbsent, Collections.singletonList(key), new Object[]{value});
    }

    public Long lPushSorted(String key, String value) {
        return (Long) this.redisTemplate.execute(this.lPushSorted, Collections.singletonList(key), new Object[]{value});
    }

    public String rPopIfNotEmpty(String key1, String key2, String ext1, String operate) {
        return !StringUtils.equalsAny(operate, new CharSequence[]{"lpush", "rpush"}) ? null : (String) this.redisTemplate.execute(this.rPopIfNotEmpty, Arrays.asList(key1, key2), new Object[]{ext1, operate});
    }

    public Set<String> zRange(String key, int start, int stop) {
        ZSetOperations<String, String> zSetOperations = this.redisTemplate.opsForZSet();
        return zSetOperations.range(key, (long) start, (long) stop);
    }

    public Set<String> sIntersect(String key, String otherKey) {
        SetOperations<String, String> stringStringSetOperations = this.redisTemplate.opsForSet();
        return stringStringSetOperations.intersect(key, otherKey);
    }

    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return this.redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    public List<String> zRangeRemByScore(String key, long min, long max) {
        return (List) this.redisTemplate.execute(this.zRangeRemByScore, Collections.singletonList(key), new Object[]{String.valueOf(min), String.valueOf(max)});
    }

    public List<String> zRangeRemByScoreAndLimit(String key, long min, long max, int offset, int count) {
        return (List) this.redisTemplate.execute(this.zRangeRemByScoreAndLimit, Collections.singletonList(key), new Object[]{String.valueOf(min), String.valueOf(max), String.valueOf(offset), String.valueOf(count)});
    }

    public Boolean zAdd(String key, String value, Number score) {
        double s;
        if(score instanceof Double) {
            s = (Double) score;
        } else {
            s = score.doubleValue();
        }

        return this.redisTemplate.opsForZSet().add(key, value, s);
    }

    public Long zAddIfAbsent(String key, String value, Number score) {
        double s;
        if(score instanceof Double) {
            s = (Double) score;
        } else {
            s = score.doubleValue();
        }

        return (Long) this.redisTemplate.execute(this.zAddIfAbsent, Collections.singletonList(key), new Object[]{value, String.valueOf(s)});
    }

    public Long zRem(String key, String value) {
        return this.redisTemplate.opsForZSet().remove(key, new Object[]{value});
    }

    public Long sRem(String key, String value) {
        return this.redisTemplate.opsForSet().remove(key, value);

    }

    public boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

}
