package cn.weihu.base.filter;

import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 过滤器
 *
 * @author liu_pc
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
           initParams = {
                   @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
           })
public class DruidStatFilter extends WebStatFilter {
    public static final Logger log = LoggerFactory.getLogger(DruidStatFilter.class);

}