package com.hq.ecmp.constant;

/**
 * @author yangnan
 */
public final class InterceptorConstant {
    public final static String NGINX = "/check.html";

    /**header拦截器不需要拦截的请求*/
    public final static String[] EXCLUDE_HEADER = {};
    /**token拦截器不需要拦截的请求*/
    public final static String[] EXCLUDE_TOKEN = {};
    /**header拦截器不需要拦截的请求*/
    public final static String[] INCLUDE_HEADER = {};

}