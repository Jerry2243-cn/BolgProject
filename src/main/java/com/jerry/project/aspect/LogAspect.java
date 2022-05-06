package com.jerry.project.aspect;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.jerry.project.web.*.*(..))")
    public void log(){}

    @Before("log()")
    public void deBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String device = getDevice(request.getHeader("User-Agent"));
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+ joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Request : {}",requestLog);
        logger.info("RequestDevice : {}",device);
    }

    @After("log()")
    public void deAfter(){
//        logger.info("-------after------");
    }

    private String getDevice(String userAgent) {
        //解析agent字符串
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        //获取浏览器对象
        Browser browser = ua.getBrowser();
        //获取操作系统对象
        OperatingSystem os = ua.getOperatingSystem();
        return String.format("{Device='%s',OS='%s',Browser-Name='%s',Browser-Version='%s',Rendering-Engine='%s',User-Agent=[%s]}",
                os.getDeviceType(),
                os.getName(),
                browser.getName(),
                browser.getVersion(userAgent),
                browser.getRenderingEngine(),
                userAgent
        );
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Result : {}",result);
    }

    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip,String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
