package cn.weihu.kol.runner;


import cn.weihu.kol.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info(">>>>>>>>>>>> KOL系统启动中,当前版本:{},站点编号:{}<<<<<<<<<<<<", Version.version);
       
    }

}