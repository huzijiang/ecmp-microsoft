package com.hq.ecmp.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hq.ecmp.constant.InterceptorConstant;
import com.hq.ecmp.constant.TraceConstant;
import com.hq.ecmp.util.DecodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

import static com.hq.ecmp.constant.TraceConstant.*;

/**
 *
 * @author yangnan
 * @date 17/9/14
 *
 * 日志跟踪
 */
public class TraceLogInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static ThreadLocal<StopWatch> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("url", "filter-" + request.getRequestURI());
        String traceId = MDC.get(TRACE_KEY);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(TRACE_KEY, traceId);
        }
        //日志中增加订单号，要求订单号参数必须是orderNo
        MDC.put(ORDER_NO, request.getParameter("orderNo"));
        String ngTraceId = MDC.get(NG_TRACE_ID);
        if (StringUtils.isBlank(ngTraceId)) {
            ngTraceId = request.getHeader(NG_TRACE_ID);
            if (StringUtils.isBlank(ngTraceId)) {
                ngTraceId = traceId;
            }
            MDC.put(TraceConstant.NG_TRACE_ID, ngTraceId);
        }
        if (InterceptorConstant.NGINX.equals(request.getRequestURI())) {
            return true;
        }
        //打印所有入参
        Enumeration<String> names = request.getParameterNames();
        if (names != null) {
            Map<String, String> params = Maps.newHashMap();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.put(name, DecodeUtil.decode(request.getParameter(name)));
            }
            logger.info("url:[{}] 请求参数:[{}]", request.getRequestURI(), JSON.toJSONString(params));
        }
        THREAD_LOCAL.set(new Slf4JStopWatch());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("api[{}] 接口耗时:[{}]ms", request.getRequestURI(), THREAD_LOCAL.get().getElapsedTime());
        MDC.remove(TRACE_KEY);
        MDC.remove(ORDER_NO);
        MDC.remove(NG_TRACE_ID);
        THREAD_LOCAL.remove();
        RequestTraceUtil.removeResponseValue();
    }
}