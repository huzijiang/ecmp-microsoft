package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 【请填写功能名称】对象 order_state_trace_info
 *
 * @author hqer
 * @date 2020-01-02
 */
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
    private BigDecimal driverLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal driverLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    public void setTraceId(Long traceId)
    {
        this.traceId = traceId;
    }

    public Long getTraceId()
    {
        return traceId;
    }
    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setDriverLongitude(BigDecimal driverLongitude)
    {
        this.driverLongitude = driverLongitude;
    }

    public BigDecimal getDriverLongitude()
    {
        return driverLongitude;
    }
    public void setDriverLatitude(BigDecimal driverLatitude)
    {
        this.driverLatitude = driverLatitude;
    }

    public BigDecimal getDriverLatitude()
    {
        return driverLatitude;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public OrderStateTraceInfo() {
    }

    public OrderStateTraceInfo(Long orderId, String state, BigDecimal driverLongitude, BigDecimal driverLatitude) {
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
