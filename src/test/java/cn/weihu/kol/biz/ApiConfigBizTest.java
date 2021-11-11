//package cn.weihu.kol.biz;
//
//import cn.weihu.kol.biz.bo.PQOtherConfig;
//import cn.weihu.kol.biz.impl.ApiConfigBizImpl;
//import cn.weihu.kol.db.po.ApiConfig;
//import cn.weihu.kol.remote.pq.SubscribeBo;
//import cn.weihu.kol.util.GsonUtils;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class ApiConfigBizTest {
//
//    private static final ApiConfigBiz biz = new ApiConfigBizImpl();
//
//    @Test
//    void init() {
//    }
//
//    @Test
//    void getInfoByType() {
//    }
//
//    @Test
//    void add() {
//        ApiConfig config = new ApiConfig();
//        config.setType(1);
//        config.setAccessId("4d5de9d18c350cdf");
//        config.setAccessSecret("67cc62c1cab7675f41c7ccf750191b5f");
//        config.setUrl("http://39.104.187.94:8088/api/v20");
//
//        PQOtherConfig otherConfig = new PQOtherConfig();
//        otherConfig.setVoiceVersion(20);
//        otherConfig.setGateway("puqiang567");
//        List<SubscribeBo> list = new ArrayList<>();
//        SubscribeBo bo1 = new SubscribeBo();
//        bo1.setType("task-complete");
//        bo1.setVersion("v20");
//        bo1.setUrl("http://124.204.36.138:6870/callback/taskComplete");
//        list.add(bo1);
//        SubscribeBo bo2 = new SubscribeBo();
//        bo2.setType("call-info");
//        bo2.setVersion("v20");
//        bo2.setUrl("http://124.204.36.138:6870/callback/callResult");
//        list.add(bo2);
//        SubscribeBo bo3 = new SubscribeBo();
//        bo3.setType("async-append");
//        bo3.setVersion("v20");
//        bo3.setUrl("http://124.204.36.138:6870/callback/appendResult");
//        list.add(bo3);
//        otherConfig.setCallback(list);
//        config.setOtherConfig(GsonUtils.gson.toJson(otherConfig));
//        System.out.println(">>> config: " + GsonUtils.gson.toJson(otherConfig));
////        biz.add(config);
//    }
//}