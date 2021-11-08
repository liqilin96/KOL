package cn.weihu.kol.controller;

import base.BasicApiJunit;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.weihu.kol.http.req.ModifyPasswordReq;
import cn.weihu.kol.util.GsonUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UserControllerTest extends BasicApiJunit {

    @Test
    void login() {
        String url = host + "/login";
        System.out.println(url);

        Map<String,Object> param = new HashMap<>(2);
        param.put("username", "admin");
        param.put("password", "weihu2021");

        HttpRequest request = HttpRequest
                .post(url)
                .form(param);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }

    @Test
    void logout() {
    }

    @Test
    void modifyPassword() {
        String url = host + "/password";
        System.out.println(url);

        ModifyPasswordReq req = new ModifyPasswordReq();
        req.setOldPassword("123456");
        req.setPassword("weihu2021");

        HttpRequest request = HttpRequest
                .put(url)
                .header("Authorization", auth)
                .body(GsonUtils.gson.toJson(req), contentType);

        System.out.println(request);
        HttpResponse response = request.execute();
        System.out.println(response);
    }
}