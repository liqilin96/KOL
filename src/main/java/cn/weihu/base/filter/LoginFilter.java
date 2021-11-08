package cn.weihu.base.filter;


import cn.weihu.base.result.ErrorCode;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.bo.CompanyBo;
import cn.weihu.kol.db.dao.UserDao;
import cn.weihu.kol.db.po.User;
import cn.weihu.kol.redis.RedisUtils;
import cn.weihu.kol.runner.StartupRunner;
import cn.weihu.kol.userinfo.UserInfo;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.EncryptUtil;
import cn.weihu.kol.util.GsonUtils;
import cn.weihu.kol.util.MD5Util;
import cn.weihu.proxy.hermes.http.BaseResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

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
               || StringUtils.startsWithAny(req.getRequestURI(), "/pns")
               || StringUtils.startsWithAny(req.getRequestURI(), "/yicall")
               || StringUtils.startsWithAny(req.getRequestURI(), "/callin")
               || StringUtils.startsWithAny(req.getRequestURI(), "/blacklist/check")
               || StringUtils.startsWithAny(req.getRequestURI(), "/api/pns")
               || StringUtils.startsWithAny(req.getRequestURI(), "/flow/sendsms")
            ) {
                chain.doFilter(request, response);
            } else if(StringUtils.startsWithAny(req.getRequestURI(), "/hermes")) {
                //
                String ak   = request.getParameter("ak");
                String ts   = request.getParameter("ts");
                String sign = request.getParameter("sign");
                if(StringUtils.isBlank(ak) || StringUtils.isBlank(ts) || StringUtils.isBlank(sign)) {
                    generateErrRes(response,
                                   BaseResponse.fail(ErrorCode.HERMES_AUTH_MISSING.getMsg()).toString());
                    return;
                }
                User user = userDao.selectOne(new LambdaQueryWrapper<>(User.class)
                                                      .eq(User::getUsername, ak));
                if(Objects.isNull(user)) {
                    generateErrRes(response,
                                   BaseResponse.fail(ErrorCode.HERMES_AUTH_ERROR.getMsg()).toString());
                    return;
                }
                String md5 = MD5Util.getMD5(ak + user.getPassword() + ts);
                if(!md5.equalsIgnoreCase(sign)) {
                    generateErrRes(response,
                                   BaseResponse.fail(ErrorCode.HERMES_AUTH_ERROR.getMsg()).toString());
                    return;
                }
                UserInfo userInfo = new UserInfo(user.getCompanyId(), user.getId(), user.getUsername(),
                                                 user.getPassword(), user.getName(), null);
                UserInfoContext.setUserInfo(userInfo);
                //
                chain.doFilter(request, response);
            } else {
                if((StringUtils.startsWithAny(req.getRequestURI(), "/api/pns")
                    || StringUtils.startsWithAny(req.getRequestURI(), "/sip/makecall"))
                   && !StartupRunner.pnsAuthFlag) {
                    // 隐私号是否鉴权判断
                    chain.doFilter(request, response);
                } else {
                    String token = request.getParameter("token");
                    if(StringUtils.isBlank(token)) {
                        token = req.getHeader("Authorization");
                    }
                    RequestWrapper      requestWrapper = null;
                    Map<String, Object> map            = null;
                    if(StringUtils.isBlank(token)
                       && !StringUtils.startsWithAny(req.getRequestURI(), "/flow/template")
                       && !StringUtils.startsWithAny(req.getRequestURI(), "/template")
                       && StringUtils.equalsAnyIgnoreCase(req.getMethod(), "POST", "PUT")) {
                        requestWrapper = new RequestWrapper(req, null);
                        String body = requestWrapper.getBody();
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        map = GsonUtils.gson.fromJson(body, type);
                        if(null != map && map.containsKey("token")) {
                            token = map.get("token").toString();
                        }
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
                    if(null != requestWrapper) {
                        if(null != map && map.containsKey("mode") && "encrypt".equalsIgnoreCase(map.get("mode").toString())) {
                            // 加密请求处理
                            // request body
                            CompanyBo config = redisUtils.getCompanyConfig(userInfo.getCompanyId());
                            String decrypt = EncryptUtil.decrypt(config.getEncryptType(),
                                                                 config.getEncryptKey(),
                                                                 map.get("data").toString());
                            requestWrapper.setBody(decrypt);
                            HttpServletResponse res             = (HttpServletResponse) response;
                            ResponseWrapper     responseWrapper = new ResponseWrapper(res);
                            chain.doFilter(requestWrapper, responseWrapper);
                            // response body
                            String encrypt = EncryptUtil.encrypt(config.getEncryptType(),
                                                                 config.getEncryptKey(),
                                                                 responseWrapper.getContent());
                            PrintWriter writer = response.getWriter();
                            writer.print(encrypt);
                            writer.flush();
                            writer.close();
                        } else {
                            chain.doFilter(requestWrapper, response);
                        }
                    } else {
                        chain.doFilter(request, response);
                    }
                }
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
