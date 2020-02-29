package com.hq.ecmp.mscore.domain;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 order_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Builder
public class OrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
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
    private String useCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualSetoutAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualSetoutLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualSetoutLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actualSetoutTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualArriveAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualArriveLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualArriveLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actualArriveTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tripartiteOrderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tripartitePlatformCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverMobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicense;

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
    public void setUseCarMode(String useCarMode)
    {
        this.useCarMode = useCarMode;
    }

    public String getUseCarMode()
    {
        return useCarMode;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setActualSetoutAddress(String actualSetoutAddress)
    {
        this.actualSetoutAddress = actualSetoutAddress;
    }

    public String getActualSetoutAddress()
    {
        return actualSetoutAddress;
    }
    public void setActualSetoutLongitude(Long actualSetoutLongitude)
    {
        this.actualSetoutLongitude = actualSetoutLongitude;
    }

    public Long getActualSetoutLongitude()
    {
        return actualSetoutLongitude;
    }
    public void setActualSetoutLatitude(Long actualSetoutLatitude)
    {
        this.actualSetoutLatitude = actualSetoutLatitude;
    }

    public Long getActualSetoutLatitude()
    {
        return actualSetoutLatitude;
    }
    public void setActualSetoutTime(Date actualSetoutTime)
    {
        this.actualSetoutTime = actualSetoutTime;
    }

    public Date getActualSetoutTime()
    {
        return actualSetoutTime;
    }
    public void setActualArriveAddress(String actualArriveAddress)
    {
        this.actualArriveAddress = actualArriveAddress;
    }

    public String getActualArriveAddress()
    {
        return actualArriveAddress;
    }
    public void setActualArriveLongitude(Long actualArriveLongitude)
    {
        this.actualArriveLongitude = actualArriveLongitude;
    }

    public Long getActualArriveLongitude()
    {
        return actualArriveLongitude;
    }
    public void setActualArriveLatitude(Long actualArriveLatitude)
    {
        this.actualArriveLatitude = actualArriveLatitude;
    }

    public Long getActualArriveLatitude()
    {
        return actualArriveLatitude;
    }
    public void setActualArriveTime(Date actualArriveTime)
    {
        this.actualArriveTime = actualArriveTime;
    }

    public Date getActualArriveTime()
    {
        return actualArriveTime;
    }
    public void setTripartiteOrderId(String tripartiteOrderId)
    {
        this.tripartiteOrderId = tripartiteOrderId;
    }

    public String getTripartiteOrderId()
    {
        return tripartiteOrderId;
    }
    public void setTripartitePlatformCode(String tripartitePlatformCode)
    {
        this.tripartitePlatformCode = tripartitePlatformCode;
    }

    public String getTripartitePlatformCode()
    {
        return tripartitePlatformCode;
    }
    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getDriverName()
    {
        return driverName;
    }
    public void setDriverMobile(String driverMobile)
    {
        this.driverMobile = driverMobile;
    }

    public String getDriverMobile()
    {
        return driverMobile;
    }
    public void setCarLicense(String carLicense)
    {
        this.carLicense = carLicense;
    }

    public String getCarLicense()
    {
        return carLicense;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("powerId", getPowerId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .append("useCarMode", getUseCarMode())
            .append("state", getState())
            .append("actualSetoutAddress", getActualSetoutAddress())
            .append("actualSetoutLongitude", getActualSetoutLongitude())
            .append("actualSetoutLatitude", getActualSetoutLatitude())
            .append("actualSetoutTime", getActualSetoutTime())
            .append("actualArriveAddress", getActualArriveAddress())
            .append("actualArriveLongitude", getActualArriveLongitude())
            .append("actualArriveLatitude", getActualArriveLatitude())
            .append("actualArriveTime", getActualArriveTime())
            .append("tripartiteOrderId", getTripartiteOrderId())
            .append("tripartitePlatformCode", getTripartitePlatformCode())
            .append("driverName", getDriverName())
            .append("driverMobile", getDriverMobile())
            .append("carLicense", getCarLicense())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
