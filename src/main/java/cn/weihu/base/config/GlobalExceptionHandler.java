package cn.weihu.base.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error(ExceptionUtils.getFullStackTrace(e));  // 记录错误信息
        String msg = e.getMessage();
        if(msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        Map<String, String> jsonObject = new HashMap<>();
        jsonObject.put("message", msg);
        return jsonObject;
    }
}