package cn.weihu.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by weihu on 2018/10/24.
 *
 * @author zyq
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidDataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private String type;

    private int maxActive;

    private int initialSize;

    private int minIdle;

    private long maxWait;

    private long timeBetweenEvictionRunsMillis;

    private long minEvictableIdleTimeMillis;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

}
