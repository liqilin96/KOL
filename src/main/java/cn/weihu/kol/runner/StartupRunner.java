package cn.weihu.kol.runner;


import cn.weihu.kol.Version;
import cn.weihu.kol.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Order(value = 2)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    public static long   SUPPLIER_USER_XIN_YI;
    public static long   SUPPLIER_USER_WEI_GE;
    public static int    PRICE_EXPIRE_REMIND_DAY;
    public static String PRICE_EXPIRE_REMIND_ROLES;

    @Value("${xin.yi.user.id}")
    public void setXinYiUserId(Long xinYiUserId) {
        StartupRunner.SUPPLIER_USER_XIN_YI = xinYiUserId;
    }

    @Value("${wei.ge.user.id}")
    public void setWeiGeUserId(Long weiGeUserId) {
        StartupRunner.SUPPLIER_USER_WEI_GE = weiGeUserId;
    }

    @Value("${price.expire.remind:30}")
    public void setPriceExpireRemindDay(Integer expireRemindDay) {
        StartupRunner.PRICE_EXPIRE_REMIND_DAY = expireRemindDay;
    }

    @Value("${price.expire.remind.roles:7}")
    public void setPriceExpireRemindRoles(String expireRemindRoles) {
        StartupRunner.PRICE_EXPIRE_REMIND_ROLES = expireRemindRoles;
    }

    @Value("${save_file_path}")
    private String saveFilePath;

    @Override
    public void run(String... args) {
        log.info(">>>>>>>>>>>> KOL系统启动中,当前版本:{}<<<<<<<<<<<<", Version.version);
        log.info(">>> 供应商XinYi用户ID:{}", SUPPLIER_USER_XIN_YI);
        log.info(">>> 供应商WeiGe用户ID:{}", SUPPLIER_USER_WEI_GE);
        log.info(">>> 保价到期时间提前提醒天数:{}", PRICE_EXPIRE_REMIND_DAY);
        log.info(">>> 保价到期提醒角色:{}", PRICE_EXPIRE_REMIND_ROLES);
        if(saveFilePath == null || saveFilePath.isEmpty()) {
            ApplicationHome home    = new ApplicationHome(this.getClass());
            File            jarFile = home.getSource();
            saveFilePath = jarFile.getParentFile().toString();
        }
        if(!saveFilePath.endsWith("/")) {
            saveFilePath += File.separator;
        }
        FileUtil.saveFilePath = saveFilePath;
        log.info(">>>>>>>> saveFilePath:{}", saveFilePath);
    }

}