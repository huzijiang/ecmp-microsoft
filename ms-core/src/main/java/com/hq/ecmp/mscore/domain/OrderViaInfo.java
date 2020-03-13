package com.hq.ecmp.mscore.domain;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 【请填写功能名称】对象 order_via_info
 * 
 * @author hqer
 * @date 2020-03-12
 */
public class OrderViaInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long viaId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double latitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String shortAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String fullAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer sortNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date arrivedTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long duration;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date leaveTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsPassed;

    public void setViaId(Long viaId) 
    {
        this.viaId = viaId;
    }

    public Long getViaId() 
    {
        return viaId;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setLongitude(Double longitude) 
    {
        this.longitude = longitude;
    }

    public Double getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(Double latitude) 
    {
        this.latitude = latitude;
    }

    public Double getLatitude() 
    {
        return latitude;
    }
    public void setShortAddress(String shortAddress) 
    {
        this.shortAddress = shortAddress;
    }

    public String getShortAddress() 
    {
        return shortAddress;
    }
    public void setFullAddress(String fullAddress) 
    {
        this.fullAddress = fullAddress;
    }

    public String getFullAddress() 
    {
        return fullAddress;
    }
    public void setSortNumber(Integer sortNumber)
    {
        this.sortNumber = sortNumber;
    }

    public Integer getSortNumber()
    {
        return sortNumber;
    }
    public void setArrivedTime(Date arrivedTime) 
    {
        this.arrivedTime = arrivedTime;
    }

    public Date getArrivedTime() 
    {
        return arrivedTime;
    }
    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }
    public void setLeaveTime(Date leaveTime) 
    {
        this.leaveTime = leaveTime;
    }

    public Date getLeaveTime() 
    {
        return leaveTime;
    }
    public void setItIsPassed(String itIsPassed) 
    {
        this.itIsPassed = itIsPassed;
    }

    public String getItIsPassed() 
    {
        return itIsPassed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("viaId", getViaId())
            .append("orderId", getOrderId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("shortAddress", getShortAddress())
            .append("fullAddress", getFullAddress())
            .append("sortNumber", getSortNumber())
            .append("arrivedTime", getArrivedTime())
            .append("duration", getDuration())
            .append("leaveTime", getLeaveTime())
            .append("itIsPassed", getItIsPassed())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
