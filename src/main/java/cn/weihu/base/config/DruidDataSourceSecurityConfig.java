package cn.weihu.base.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by weihu on 2018/10/24.
 *
 * @author zyq
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = {"spring.datasource.password", "security.mysql.key"})
public class DruidDataSourceSecurityConfig {

    @Value("${security.mysql.key}")
    private String securityKey;

    @Bean
    public DruidDataSourceProperties druidDataSourceProperties() {
        return new DruidDataSourceProperties();
    }

    @Bean
    public DataSource dataSource(DruidDataSourceProperties properties) {
        log.info(">>> 检测到加密配置!");

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());

        try {
            dataSource.setPassword(new Decryptor(securityKey).decrypt(properties.getPassword()));
        } catch(Exception e) {
            log.error(">>> 解密失败!", e);
            dataSource.setPassword(properties.getPassword());
        }

        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setMaxActive(properties.getMaxActive() == 0 ? 10 : properties.getMaxActive());
        dataSource.setInitialSize(properties.getInitialSize());
        dataSource.setMinIdle(properties.getMinIdle());
        dataSource.setMaxWait(properties.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        dataSource.setTestWhileIdle(properties.isTestWhileIdle());
        dataSource.setTestOnBorrow(properties.isTestOnBorrow());
        dataSource.setTestOnReturn(properties.isTestOnReturn());
        dataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        return dataSource;
    }

}
