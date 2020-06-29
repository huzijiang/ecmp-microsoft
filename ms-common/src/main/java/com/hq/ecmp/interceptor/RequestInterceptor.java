package com.hq.ecmp.interceptor;

import com.hq.ecmp.interceptor.log.ParamsLogger;
import com.hq.ecmp.util.IpUtil;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yangnan
 * @Date: 2018/8/17 10:48
 * @Description: 请求过滤器
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static ThreadLocal<StopWatch> local = new NamedThreadLocal<StopWatch>("RequestInterceptor");

    private final Logger switchLogger = LoggerFactory.getLogger("org.perf4j.TimingLogger");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new Slf4JStopWatch("shell");
        local.set(stopWatch);
        ParamsLogger.printTraceLog(request);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch watch = local.get();
        long startTime = watch.getStartTime();
        ParamsLogger.printEndTraceLog(startTime, request, response);
        RequestTraceUtil.removeResponseValue();
        if (watch != null) {
            watch.stop(generateOperatonIdendifier(request, watch.getElapsedTime()));
            local.remove();
        }
    }

    private String generateOperatonIdendifier(HttpServletRequest request, long exeTime) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(request.getMethod()).append("|");

        // 如果是trace级别，统计到具体的URI
        if (switchLogger.isTraceEnabled()) {
            sb.append(request.getRequestURL());
            sb.append('|');
            String clientIp = IpUtil.getIp(request);
            sb.append(clientIp);
            sb.append('|');
            sb.append(request.getHeader("User-Agent"));
        } else { // 按URI pattern匹配，方便汇总
            sb.append(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        }

        // 记录慢得url,
        if (exeTime > 100) {
            sb.append("|SLOW");
        }

        return sb.toString();
    }
}