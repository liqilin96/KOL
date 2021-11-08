package cn.weihu.kol.db.config;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan("cn.weihu.**.db.dao")
@Configuration
public class MyBatisConfig {
    /**
     * 配置mybatis的分页插件pageHelper
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setOverflow(false);
        paginationInterceptor.setDialect(new MySqlDialect());
        paginationInterceptor.setLimit(10000L);
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize());
        return paginationInterceptor;
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }
}  