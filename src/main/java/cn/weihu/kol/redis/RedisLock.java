package cn.weihu.kol.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class RedisLock implements Closeable {

    private static final Long          SUCCESS = 1L;
    private              RedisTemplate redisTemplate;
    private              String        lockKey;
    private              String        lockValue;
    private              int           expireTime;

    public RedisLock(RedisTemplate redisTemplate, String lockKey, String lockValue, int expireTime) {
        this.redisTemplate = redisTemplate;
        //redis key
        this.lockKey = lockKey;
        //redis value
        this.lockValue = lockValue;
        //过期时间 单位：s
        this.expireTime = expireTime;
    }

    public static void main(String[] args) {

    }

    public boolean getLock() {
        boolean ret = false;
        try {
            String              script      = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
            RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
            Object              result      = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue, expireTime);
            if(SUCCESS.equals(result)) {
                return true;
            }
        } catch(Exception e) {

        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        //释放锁的lua脚本
        String              script      = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
        //是否redis锁
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue);
        log.info("释放redis锁结果：" + result);
    }
}