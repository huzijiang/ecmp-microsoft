package com.hq.ecmp.interceptor.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hq.ecmp.interceptor.RequestTraceUtil;
import com.hq.ecmp.pojo.dto.TraceItemDTO;
import com.hq.ecmp.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;
import static com.hq.ecmp.constant.InterceptorConstants.*;

/**
 * @author yangnan
 * @Auther: maguosheng
 * @Date: 2018/8/29 11:47
 */
public class ParamsLogger {
    private static final Logger logger = LoggerFactory.getLogger(ParamsLogger.class);

    private static final SerializerFeature[] FEATURES = {DisableCircularReferenceDetect, WriteMapNullValue};

    public static void printTraceLog(HttpServletRequest request) {
        try {
            ParamsRequestLog requestLog = new ParamsRequestLog();

            Map<String, String[]> pramMap = request.getParameterMap();
            Map<String, String> formMap = new HashMap<String, String>();
            for (Map.Entry<String, String[]> entry : pramMap.entrySet()) {
                formMap.put(entry.getKey(), request.getParameter(entry.getKey()));
            }
            requestLog.setFormValues(formMap);
            Map<String, String> headerMap = new HashMap<String, String>();
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headerMap.put(key, value);
            }
            headerMap.put(DEFAULT_HEADER_REQUESTURL, request.getRequestURL().toString());
            headerMap.put(DEFAULT_HEADER_METHOD, request.getMethod());
            requestLog.setHeaders(headerMap);
            requestLog.setLogType(LOGTYPE_REQUEST);
            requestLog.setNgTraceId(TraceItemDTO.getNgTraceIdStr());
            requestLog.setClientIp(IpUtil.getIp(request));
            requestLog.setTraceId(TraceItemDTO.getTraceIdStr());
            logger.info(JSONObject.toJSONString(requestLog));
        } catch (Exception e) {
            logger.error(INTERCEPTOR_DEFAULT_ERROR_MSG, e);
        }

    }

    public static void printEndTraceLog(long startTime, HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            ParamsResponseLog responseLog = new ParamsResponseLog();
            responseLog.setResponseBody(getResponseData(response));
            responseLog.setLogType(LOGTYPE_RESPONSE);
            responseLog.setNgTraceId(TraceItemDTO.getNgTraceIdStr());
            responseLog.setTimeConsuming(System.currentTimeMillis() - startTime);
            responseLog.setTraceId(TraceItemDTO.getTraceIdStr());
            responseLog.setUrl(request.getRequestURL().toString());
            responseLog.setMethod(request.getMethod());
            logger.info(JSON.toJSONString(responseLog, FEATURES));
        } catch (Exception e) {
            logger.error(INTERCEPTOR_DEFAULT_ERROR_MSG, e);
        }

    }

    /**
     * 获取响应参数
     *
     * @param response
     * @return
     * @throws Exception
     */
    private static Object getResponseData(HttpServletResponse response) throws Exception {
        return RequestTraceUtil.getResponseValue();
    }
}
