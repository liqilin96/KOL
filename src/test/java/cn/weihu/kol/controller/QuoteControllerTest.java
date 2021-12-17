package cn.weihu.kol.controller;

import base.BasicApiJunit;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lql
 * @date 2021/12/17 13:36
 * Description：
 */
public class QuoteControllerTest extends BasicApiJunit {


    @Test
    void priceDetailPage() {
        String url = host + "/quote/price/detail";
        System.out.println(url);

        Map<String, Object> param = new HashMap<>(1);
        param.put("actorSn", "551be164d1dd0786852711c426574bc6");

        HttpRequest request = HttpRequest
                .get(url)
                .form(param);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }


    @Test
    void quotePage() {
        String url = host + "/quote/page";
        System.out.println(url);

        Map<String, Object> param = new HashMap<>(2);
        param.put("starName", "螳螂");
        param.put("platform", "小红书");

        HttpRequest request = HttpRequest
                .get(url)
                .form(param);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }
}
