package cn.weihu.kol.container;

import cn.weihu.kol.db.po.PlatformRules;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class PlatformRulesContainer {

    public static ConcurrentHashMap<String, Map<String, String>> platformRulesMap = new ConcurrentHashMap<>();

    public static void initPlatformRules(List<PlatformRules> list) {
        Map<String, Map<String, String>> map = list.stream()
                .collect(Collectors.groupingBy(PlatformRules::getPlatform,
                                               Collectors.toMap(PlatformRules::getAddress, PlatformRules::getScreenFields)));
        for(Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            platformRulesMap.put(entry.getKey(), entry.getValue());
        }
    }

    public static String getScreenFields(String platform, String address) {
        Map<String, String> map = platformRulesMap.get(platform);
        if(!CollectionUtils.isEmpty(map)) {
            return map.get(address);
        }
        return null;
    }
}
