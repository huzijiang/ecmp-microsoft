package com.hq.ecmp.interceptor;

import org.springframework.core.NamedThreadLocal;

/**
 * @author yangnan
 */
public class RequestTraceUtil {
    private static ThreadLocal<Object> RESPONSE_LOCAL = new NamedThreadLocal<>("ResponseTrace");
    private static ThreadLocal<String> REQUEST_LOCAL = new NamedThreadLocal<>("RequestTrace");

    /**
     * 设置请求中
     * @param value
     */
    public static void setRequestValue(String value) {
        REQUEST_LOCAL.set(value);
    }
    /**
     * 设置http响应值
     * @param value
     */
    public static void setResponseValue(Object value) {
        RESPONSE_LOCAL.set(value);
    }

    /**
     * 获取数据
     * @return
     */
    public static Object getResponseValue() {
        return RESPONSE_LOCAL.get();
    }

    /**
     * 获取数据
     * @return
     */
    public static String getRequestValue() {
        return REQUEST_LOCAL.get();
    }

    /**
     * 删除
     */
    public static void removeResponseValue() {
        RESPONSE_LOCAL.remove();
    }

    /**
     * 删除数据
     */
    public static void removeRequestValue() {
        REQUEST_LOCAL.remove();
    }
}
