package cn.weihu.kol.job;

import cn.hutool.core.date.DateUtil;
import cn.weihu.kol.biz.MessageBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.RoleUserBiz;
import cn.weihu.kol.db.po.Message;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.db.po.RoleUser;
import cn.weihu.kol.runner.StartupRunner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MonitorJob {

    @Autowired
    private PricesBiz   pricesBiz;
    @Autowired
    private MessageBiz  messageBiz;
    @Autowired
    private RoleUserBiz roleUserBiz;

    @Scheduled(cron = "${check.price.cron:0 0 0 * * ?}")
    public void priceExpire() {
        // 保价到期检测
        // 提前一个月
        Date compareTime = DateUtil.offsetDay(DateUtil.date(), -StartupRunner.PRICE_EXPIRE_REMIND_DAY);
        List<Prices> list = pricesBiz.list(new LambdaQueryWrapper<>(Prices.class)
                                                   .ge(Prices::getInsureEndtime, compareTime));
        if(!CollectionUtils.isEmpty(list)) {
            log.info(">>> 存在一个月后保价到期数据...size:{}", list.size());
            // 获取提醒用户
            if(StringUtils.isBlank(StartupRunner.PRICE_EXPIRE_REMIND_ROLES)) {
                log.warn(">>> 消息提醒角色配置为空...");
            }
            String[] roleIds = StringUtils.split(StartupRunner.PRICE_EXPIRE_REMIND_ROLES, ",");
            List<String> userIds = roleUserBiz.list(new LambdaQueryWrapper<>(RoleUser.class)
                                                            .in(RoleUser::getRoleId, roleIds))
                    .stream()
                    .map(RoleUser::getUserId)
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(userIds)) {
                log.warn(">>> 消息提醒角色配置错误...");
            }
            // 生成提醒消息
            List<Message> messageList = new ArrayList<>();
            Message       message;
            for(String userId : userIds) {
                message = new Message();
                message.setMessage("系统中有" + list.size() + "条保价信息即将到期,请及时查看处理");
                message.setType("1");
                message.setToUserId(Long.parseLong(userId));
                message.setIsReceived(0);
                message.setCtime(LocalDateTime.now());
                message.setUtime(LocalDateTime.now());
            }
            messageBiz.saveBatch(messageList);
            log.info(">>> 保价即将到期提醒消息已生成...");
        } else {
            log.info(">>> 不存在一个月后保价到期数据...");
        }
    }
}
