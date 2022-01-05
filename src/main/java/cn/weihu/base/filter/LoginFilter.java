package cn.weihu.base.filter;


import cn.weihu.base.result.ErrorCode;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.db.dao.UserDao;
import cn.weihu.kol.redis.RedisUtils;
import cn.weihu.kol.userinfo.UserInfo;
import cn.weihu.kol.userinfo.UserInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("loginFilter")
public class LoginFilter implements Filter {

    public static final  String KEY_REQUESTID    = "requestId";
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Autowired
    RedisUtils redisUtils;
    @Resource
    UserDao    userDao;

    @Override
    public void init(FilterConfig conf) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            response.setContentType("application/json;charset=UTF-8");
            req.setCharacterEncoding(DEFAULT_ENCODING);
            response.setCharacterEncoding(DEFAULT_ENCODING);
            request.setCharacterEncoding(DEFAULT_ENCODING);

            if(StringUtils.startsWithAny(req.getRequestURI(), "/login")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/token")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/auth")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/api/token")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/prometheus")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/pro")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/swagger")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/v2/api-docs")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/file")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/callback")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/logout")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/workorder/download")
            ) {
                chain.doFilter(request, response);
            } else {
                String token = request.getParameter("token");
                if(StringUtils.isBlank(token)) {
                    token = req.getHeader("Authorization");
                }
                if(StringUtils.isBlank(token)) {
                    generateErrRes(response,
                            new ResultBean(ErrorCode.NO_AUTH).toString());
                    return;
                }
                UserInfo userInfo = redisUtils.getUserInfoByToken(token);
                if(userInfo == null) {
                    generateErrRes(response,
                            new ResultBean(ErrorCode.NO_AUTH).toString());
                    return;
                }
                UserInfoContext.setUserInfo(userInfo);
                chain.doFilter(request, response);
            }
        } finally {
            //本次请求结束,清除此线程占用
            UserInfoContext.release();
        }
    }

    private void generateErrRes(ServletResponse response, String msg) {
        try {
            ((HttpServletResponse) response).setHeader("Content-type", "application/json;charset=UTF-8");
            ((HttpServletResponse) response).setStatus(403);
            response.getWriter().write(msg);
            response.getWriter().flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }

}
