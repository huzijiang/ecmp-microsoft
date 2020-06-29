package com.hq.ecmp.pojo.dto;

import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

import static com.hq.ecmp.constant.TraceConstant.*;

/**
 *
 * @author yangnan
 * @date 17/9/7
 *
 * MDC中存放的记录，便于查询日志
 *
 */
@Data
public class TraceItemDTO implements Serializable {
    private static final long serialVersionUID = 3697722045461880439L;

    /**请求traceId*/
    private String traceId;
    /**订单号*/
    private String orderNo;
    /**链条调用关系*/
    private String ngTraceId;
    private String grade;

    public TraceItemDTO(String traceId, String orderNo, String ngTraceId) {
        this.traceId = traceId;
        this.ngTraceId = ngTraceId;
        this.orderNo = orderNo;
    }

    /**
     * 基于当前MDC创建item
     *
     * @return
     */
    public static TraceItemDTO createByCurrentMDC() {
        return new TraceItemDTO(MDC.get(TRACE_KEY), MDC.get(ORDER_NO), MDC.get(NG_TRACE_ID));
    }

    /**
     * 往当前线程的MDC中放值
     */
    public void putALl() {
        MDC.put(TRACE_KEY, getTraceId());
        MDC.put(ORDER_NO, getOrderNo());
        MDC.put(NG_TRACE_ID, getNgTraceId());
    }

    /**
     * 移除MDC中的值
     */
    public void removeAll() {
        MDC.remove(TRACE_KEY);
        MDC.remove(ORDER_NO);
        MDC.remove(NG_TRACE_ID);
    }

    /**
     * 获取traceId
     *
     * @return
     */
    public static String getTraceIdStr() {
        return MDC.get(TRACE_KEY);
    }

    /**
     * 获取orderNo
     *
     * @return
     */
    public static String getOrderNoStr() {
        return MDC.get(ORDER_NO);
    }

    /**
     * 获取ngTraceId
     *
     * @return
     */
    public static String getNgTraceIdStr() {
        return MDC.get(NG_TRACE_ID);
    }
}
