package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【请填写功能名称】对象 journey_plan_price_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class JourneyPlanPriceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long priceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long nodeId;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carTypeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal price;

    private Date plannedDepartureTime;
    private Integer duration;
    private Date plannedArrivalTime;

    public void setPriceId(Long priceId)
    {
        this.priceId = priceId;
    }

    public Long getPriceId()
    {
        return priceId;
    }
    public void setJourneyId(Long journeyId)
    {
        this.journeyId = journeyId;
    }

    public Long getJourneyId()
    {
        return journeyId;
    }
    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }

    public Long getNodeId()
    {
        return nodeId;
    }
    public void setCarTypeId(Long carTypeId)
    {
        this.carTypeId = carTypeId;
    }

    public Long getCarTypeId()
    {
        return carTypeId;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getPlannedDepartureTime() {
        return plannedDepartureTime;
    }

    public void setPlannedDepartureTime(Date plannedDepartureTime) {
        this.plannedDepartureTime = plannedDepartureTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getPlannedArrivalTime() {
        return plannedArrivalTime;
    }

    public void setPlannedArrivalTime(Date plannedArrivalTime) {
        this.plannedArrivalTime = plannedArrivalTime;
    }

    public JourneyPlanPriceInfo() {
    }

    public JourneyPlanPriceInfo(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("priceId", getPriceId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("carTypeId", getCarTypeId())
            .append("price", getPrice())
            .toString();
    }
}
