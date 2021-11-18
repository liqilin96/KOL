package cn.weihu.kol.controller;

import base.BasicApiJunit;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class WorkOrderDataControllerTest extends BasicApiJunit {

    @Test
    void workOrderDataList() {
        String url = host + "/workorder/data/list";
        System.out.println(url);

        Map<String, Object> param = new HashMap<>();
        param.put("workOrderId", "1");

        HttpRequest request = HttpRequest
                .get(url)
                .header("Authorization", auth)
                .form(param);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }

    @Test
    void updateWorkOrderData() {
    }

    @Test
    void screening() {
    }

    @Test
    void enquiry() {
    }

    @Test
    void quote() {
    }

    @Test
    void review() {
    }
}