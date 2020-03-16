package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 order_address_info
 * 
 * @author hqer
 * @date 2020-03-16
 */
public class OrderAddressInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long orderAddressId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long nodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long powerId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityPostalCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actionTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String addressLong;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double latitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String icaoCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    public void setOrderAddressId(Long orderAddressId) 
    {
        this.orderAddressId = orderAddressId;
    }

    public Long getOrderAddressId() 
    {
        return orderAddressId;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
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
    public void setPowerId(Long powerId) 
    {
        this.powerId = powerId;
    }

    public Long getPowerId() 
    {
        return powerId;
    }
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setCarId(Long carId) 
    {
        this.carId = carId;
    }

    public Long getCarId() 
    {
        return carId;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setCityPostalCode(String cityPostalCode) 
    {
        this.cityPostalCode = cityPostalCode;
    }

    public String getCityPostalCode() 
    {
        return cityPostalCode;
    }
    public void setActionTime(Date actionTime) 
    {
        this.actionTime = actionTime;
    }

    public Date getActionTime() 
    {
        return actionTime;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setAddressLong(String addressLong) 
    {
        this.addressLong = addressLong;
    }

    public String getAddressLong() 
    {
        return addressLong;
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
    public void setIcaoCode(String icaoCode) 
    {
        this.icaoCode = icaoCode;
    }

    public String getIcaoCode() 
    {
        return icaoCode;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderAddressId", getOrderAddressId())
            .append("orderId", getOrderId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("powerId", getPowerId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .append("userId", getUserId())
            .append("cityPostalCode", getCityPostalCode())
            .append("actionTime", getActionTime())
            .append("address", getAddress())
            .append("addressLong", getAddressLong())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("icaoCode", getIcaoCode())
            .append("type", getType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
