package cn.weihu.kol.runner;


import cn.weihu.kol.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    public static long SUPPLIER_USER_XIN_YI;
    public static long SUPPLIER_USER_WEI_GE;

    @Value("${xin.yi.user.id}")
    public void setXinYiUserId(Long xinYiUserId) {
        StartupRunner.SUPPLIER_USER_XIN_YI = xinYiUserId;
    }

    @Value("${wei.ge.user.id}")
    public void setWeiGeUserId(Long weiGeUserId) {
        StartupRunner.SUPPLIER_USER_WEI_GE = weiGeUserId;
    }

    @Override
    public void run(String... args) {
        log.info(">>>>>>>>>>>> KOL系统启动中,当前版本:{}<<<<<<<<<<<<", Version.version);
        log.info(">>> 供应商XinYi用户ID:{}", StartupRunner.SUPPLIER_USER_XIN_YI);
        log.info(">>> 供应商WeiGe用户ID:{}", StartupRunner.SUPPLIER_USER_WEI_GE);
    }

}