package cn.weihu.base.exception;

import cn.weihu.base.result.ErrorCode;
import cn.weihu.base.result.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spirngmvc自带的全局异常处理类,
 * 当业务逻辑抛出异常时都会被该类拦截并进行处理.
 *
 * @Author xxx
 * @Version 1.0
 * @see
 */

@Order(-1000)
@Slf4j
public class BaseExceptionHandler implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView            mav  = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        ResultBean<String>      bean = null;
        ex.printStackTrace();
        if(ex instanceof CheckException) {
            bean = new ResultBean<>(((CheckException) ex).getCode(), ex.getMessage());
        } else {
            ex.printStackTrace();
            log.info("系统异常=============================");
            bean = new ResultBean<>(ErrorCode.ERROR);
        }
        view.setAttributesMap(bean.toMap());
        mav.setView(view);
        return mav;
    }
}
