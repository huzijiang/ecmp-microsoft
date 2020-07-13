package com.hq.ecmp.ms.api.ScheduledJobs;


import com.hq.ecmp.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * @author liuliang
 * @date 2020/7/13 上午10:17
 */
@Aspect
@Component
public class IpAspect {
    private static final Logger log = LoggerFactory.getLogger(IpAspect.class);

    @Pointcut(value = "execution(* com.hq.ecmp.ms.api.ScheduledJobs.ScheduledTask.*(..))")
    public void exec() {
    }
    @Before("exec()")
    public void doBefore(JoinPoint joinPoint) {
        String name = joinPoint.getSignature().getName();
        log.info("定时任务拦截=={}==开始执行",name);
    }

    @Around("exec()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            Object ret = null;
            String localIp = IpUtil.getLocalIP();
            if (!"10.32.10.31".equals(localIp)) {
                // 执行方法
                ret = proceedingJoinPoint.proceed();
                log.warn("定时任务拦截在服务器{}开始执行",localIp);
            } else {
                log.warn("定时任务拦截在服务器{}拒绝执行",localIp);
            }
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
