package com.hq.ecmp.interceptor.log;

import lombok.Data;

/**
 * @author yangnan
 */
@Data
public class ParamsResponseLog {
    private Object responseBody;
    private long timeConsuming;
    private String swTraceId;
    private String ngTraceId;
    private String logType;
    private String traceId;
    private String url;
    private String method;
}
