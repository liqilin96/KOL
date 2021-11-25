package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.PlatformRulesBiz;
import cn.weihu.kol.container.PlatformRulesContainer;
import cn.weihu.kol.db.dao.PlatformRulesDao;
import cn.weihu.kol.db.po.PlatformRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 平台筛选规则 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-25
 */
@Slf4j
@Service
public class PlatformRulesBizImpl extends BaseBiz<PlatformRulesDao, PlatformRules> implements PlatformRulesBiz {

    @Override
    public void init() {
        List<PlatformRules> list = list();
        if(!CollectionUtils.isEmpty(list)) {
            PlatformRulesContainer.initPlatformRules(list);
            log.info(">>> 初始化平台筛选规则数据完成...size:{}", list.size());
        } else {
            log.warn(">>> 平台筛选规则数据不存在,初始化失败");
        }
    }
}
