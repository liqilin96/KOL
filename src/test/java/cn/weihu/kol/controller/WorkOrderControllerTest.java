package cn.weihu.kol.controller;

import base.BasicApiJunit;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class WorkOrderControllerTest extends BasicApiJunit {

    @Test
    void workOrderPage() {
        String url = host + "/workorder/page";
        System.out.println(url);

        Map<String, Object> param = new HashMap<>(2);
        param.put("pageNo", "1");
        param.put("pageSize", "10");

        HttpRequest request = HttpRequest
                .get(url)
                .header("Authorization", auth)
                .form(param);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }
}