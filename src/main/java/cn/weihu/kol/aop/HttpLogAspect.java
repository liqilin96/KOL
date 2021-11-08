package cn.weihu.kol.aop;

import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.SystemLogService;
import cn.weihu.kol.db.po.SystemLog;
import cn.weihu.kol.redis.RedisUtils;
import cn.weihu.kol.userinfo.UserInfo;
import cn.weihu.kol.util.DateTimeUtils;
import cn.weihu.kol.util.ExceptionUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.prometheus.client.Gauge;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
//指定这是一个其切面
@Slf4j
public class HttpLogAspect {
    public static final Gauge                             redisErrorRequests = Gauge.build()
            .name("errormsg").labelNames("type")
            .help("Inprogress requests.").register();

    ExecutorService insertDBExecutors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static       ConcurrentMap<String, AtomicLong> redisstat          = new ConcurrentHashMap<>();
    public static       ConcurrentMap<String, AtomicLong> redisstattime      = new ConcurrentHashMap<>();
    static              Gson                              gson               = new GsonBuilder().disableHtmlEscaping().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            //过滤掉字段名包含"draw"
            return f.getName().contains("draw");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            //过滤掉 类名包含 Bean的类
            return clazz.getName().contains("org.apache") || clazz.getName().contains("org.springframework");
        }
    }).create();


    @Resource
    RedisUtils redisUtils;
    @Autowired
    SystemLogService service;

    @Around(value = "execution(public * cn.weihu.*.controller..*.*(..)) ")
    public Object aroundMethod2(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest       request           = null;
        SystemLog                systemLog         = new SystemLog();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            request = requestAttributes.getRequest();
        }
        StringBuilder lc = new StringBuilder();
        lc.append("\t请求内容:\t");
        if(request != null) {
            lc.append("请求地址:").append(request.getRequestURL().toString()).append("\t");
            lc.append("请求方式:").append(request.getMethod()).append("\t");
            String   remoteAddr = request.getRemoteAddr();
            String   token      = request.getHeader("Authorization");
            UserInfo userInfo   = redisUtils.getUserInfoByToken(token);
            systemLog.setCompanyId(userInfo == null ? "" : userInfo.getCompanyId());
            systemLog.setUserName(userInfo == null ? "" : userInfo.getUsername());
            systemLog.setIp(remoteAddr);

        }
        lc.append("请求类方法:").append(point.getSignature()).append("\t");
        lc.append("请求类方法参数:").append(gson.toJson(point.getArgs())).append("\t");

        //记录操作接口和入参
        systemLog.setInParams(gson.toJson(point.getArgs()));
        systemLog.setOperationInterface(point.getSignature().toString());

        long          start  = System.currentTimeMillis();
        ResultBean<?> result = null;
        try {
            result = (ResultBean<?>) point.proceed();
        } catch(Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            result = handlerException(point, e);
        }

        long end = System.currentTimeMillis();
        lc.append("请求时长:").append((end - start)).append("(ms)\t");
        String ret = result == null ? "" : result.toString();
        lc.append("响应内容:\t");
        lc.append("响应长度:").append(ret.length()).append("(char)\t");
        if(ret.length() > 2000) {
            ret = ret.substring(0, 2000) + "...length=" + ret.length();
        }
        lc.append("响应内容:").append(ret).append("\t");

        systemLog.setReturnValue(ret);
        systemLog.setOperationTime(LocalDateTime.now());

        if(!"GET".equalsIgnoreCase(request.getMethod())){
            insertDBExecutors.execute(() ->{
                //操作日志记录
                service.insertSystemLog(systemLog);
            });
        }
        log.info(lc.toString());
        return result;
    }

    private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean();    // 已知异常
        if(e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(((CheckException) e).getCode());
        } else {
            log.error(pjp.getSignature() + " error ", e);
            result.setMsg("系统异常!");
            result.setCode(ResultBean.FAIL);
            // 未知异常是应该重点关注的,这里可以做其他操作,如通知邮件,单独写到某个文件等等。
        }
        return result;
    }

    @Around(value = "execution(* cn.weihu.kol.redis..*.*(..))")
    public Object aroundMethod_reidis(ProceedingJoinPoint point) throws Throwable {
        long          start     = System.currentTimeMillis();
        StringBuilder lc        = new StringBuilder();
        String        shortname = point.getSignature().toShortString();
        lc.append(DateTimeUtils.getDate("yyyy-MM-dd HH:mm:ss SSS")).append(" :redis方法:").append(shortname);
        lc.append(",参数:").append(gson.toJson(point.getArgs()));

//        log.info(lc.toString());
        Object result = null;
        try {
            result = point.proceed();
        } catch(Exception e) {
            lc.append(",时长:").append((System.currentTimeMillis() - start)).append("(ms)");
            log.error(lc.toString() + "-----" + ExceptionUtil.getMessage(e));
            redisErrorRequests.labels("redis执行异常").inc();
//            result = handlerException(point, e);
            try {
                Thread.sleep(500);
                result = point.proceed();
            } catch(Exception e1) {
                lc.append(",重试一次,时长:").append((System.currentTimeMillis() - start)).append("(ms)");
                log.error(lc.toString() + "-----" + ExceptionUtil.getMessage(e));
            }
        }
        long end = System.currentTimeMillis();
        lc.append(",时长:").append((end - start)).append("(ms)");
        lc.append(",返回数据:").append(gson.toJson(result));
//        redisstat(shortname,end,start);

        if((end - start) > 200 && shortname.indexOf("blpop") == -1) {
            log.warn(lc.toString());
        } else if(shortname.indexOf("taskBatch_pdsConfig_get") == -1) {
            log.debug(lc.toString());
        }
//        PrintLogThread.queue.add(lc.toString());
        return result;
    }

    private void redisstat(String shortname, long end, long start) {
        try {
            AtomicLong num = redisstat.get(shortname);
            if(num == null) {
                num = new AtomicLong(0);
                redisstat.put(shortname, num);
            }
            num.incrementAndGet();
            AtomicLong time = redisstattime.get(shortname);
            if(time == null) {
                time = new AtomicLong(0);
                redisstattime.put(shortname, time);
            }
            time.addAndGet(end - start);
        } catch(Exception e) {
        }
    }

}
