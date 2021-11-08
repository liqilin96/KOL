package cn.weihu.kol.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    private static final long timeout = 5000;

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisConfigA() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @Primary
    public LettuceConnectionFactory factoryA(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigA) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(timeout)).build();
        return new LettuceConnectionFactory(redisConfigA, clientConfiguration);
    }


    @Bean(name = "redisTemplateA")
    public StringRedisTemplate redisTemplateA(@Qualifier("factoryA") LettuceConnectionFactory factoryA) {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryA);
        return template;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.two")
    public RedisStandaloneConfiguration redisConfigB() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public LettuceConnectionFactory factoryB(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigB) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(timeout)).build();
        return new LettuceConnectionFactory(redisConfigB, clientConfiguration);
    }


    @Bean(name = "redisTemplateB")
    public StringRedisTemplate redisTemplateB(@Qualifier("factoryB") LettuceConnectionFactory factoryB) {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryB);
        return template;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.three")
    public RedisStandaloneConfiguration redisConfigC() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public LettuceConnectionFactory factoryC(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigC) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(timeout)).build();
        return new LettuceConnectionFactory(redisConfigC, clientConfiguration);
    }


    @Bean(name = "redisTemplateC")
    public StringRedisTemplate redisTemplateC(@Qualifier("factoryC") LettuceConnectionFactory factoryC) {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryC);
        return template;
    }


    private StringRedisTemplate getRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

}
