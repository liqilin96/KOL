package cn.weihu.base.controller;

import cn.weihu.base.result.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Controller
@RequestMapping("/error")
public class ErrorController {
    public static final  Logger log        = LoggerFactory.getLogger(ErrorController.class);
    private static final String ERROR_PATH = "/";

    //    @RequestMapping(value ="/{errorcode}" )
//    public ResultBean<String> error500(ServletRequest request, ServletResponse rsp,@PathVariable(value="errorcode") Integer errorcode) { 
//    	log.info("errorcode:{}",errorcode);
//    	 return new ResultBean<String>(errorcode,"123123");  
//    } 
//    @RequestMapping(value =ERROR_PATH )  
//    public ResultBean<String> handleError(ServletRequest request, ServletResponse rsp) {  
//        log.info("其它错误~");  
//        return new ResultBean<String>(500,"其它错误~");  
//    }  
    @RequestMapping(value = "/{errorcode}")
    @ResponseBody
    public ResultBean<String> error404(ServletRequest request, ServletResponse rsp, @PathVariable(value = "errorcode") String errorcode) {
        return new ResultBean<String>(errorcode, HttpStatus.valueOf(errorcode).getReasonPhrase());
    }  
   /*
    @RequestMapping(value ="/403" )  
    public String error403(ServletRequest request, ServletResponse rsp) {  
        log.info("403错误~");  
        return "error/403";  
    }  
   
    @RequestMapping(value ="/500" )  
    public ResultBean<String> error500(ServletRequest request, ServletResponse rsp) {  
    	 return new ResultBean<String>(500,"500错误~");  
    }  
   
    @RequestMapping(value ="/locked" )  
    public String errorlocked(ServletRequest request, ServletResponse rsp) {  
        log.info("locked~");  
        return "error/locked";  
    }  
    @RequestMapping(value ="/noauth" )  
    public String errornoauth(ServletRequest request, ServletResponse rsp) {  
        log.info("noauth~");  
        return "error/noauth";  
    }  */
}  