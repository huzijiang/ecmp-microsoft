package com.hq.ecmp.interceptor.log;

import lombok.Data;

import java.util.Map;

/**
 * @author yangnan
 * @Date: 2018/8/28 16:42
 */
@Data
public class ParamsRequestLog {
    private Map<String, String> headers;
    private Map<String, String> formValues;
    private String logType;
    private String swTraceId;
    private String ngTraceId;
    private String traceId;
    /**来源ip*/
    private String clientIp;

}
