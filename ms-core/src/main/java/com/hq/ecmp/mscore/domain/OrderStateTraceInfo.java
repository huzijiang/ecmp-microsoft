package com.hq.ecmp.mscore.domain;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 order_state_trace_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@Builder
@AllArgsConstructor
public class OrderStateTraceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long traceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")

    private Double driverLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double driverLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;


    public OrderStateTraceInfo() {
    }

    public OrderStateTraceInfo(Long orderId, String state) {
        this.orderId = orderId;
        this.state = state;
    }

    public OrderStateTraceInfo(Long orderId, String state, Double driverLongitude, Double driverLatitude) {
        this.orderId = orderId;
        this.state = state;
        this.driverLongitude = driverLongitude;
        this.driverLatitude = driverLatitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("traceId", getTraceId())
            .append("orderId", getOrderId())
            .append("state", getState())
            .append("driverLongitude", getDriverLongitude())
            .append("driverLatitude", getDriverLatitude())
            .append("content", getContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
