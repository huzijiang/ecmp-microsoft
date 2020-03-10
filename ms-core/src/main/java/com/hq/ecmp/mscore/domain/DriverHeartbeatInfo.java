package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 driver_heartbeat_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class DriverHeartbeatInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long heartId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long latitude;

    public void setHeartId(Long heartId)
    {
        this.heartId = heartId;
    }

    public Long getHeartId()
    {
        return heartId;
    }
    public void setDriverId(Long driverId)
    {
        this.driverId = driverId;
    }

    public Long getDriverId()
    {
        return driverId;
    }
    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }
    public void setLongitude(Long longitude)
    {
        this.longitude = longitude;
    }

    public Long getLongitude()
    {
        return longitude;
    }
    public void setLatitude(Long latitude)
    {
        this.latitude = latitude;
    }

    public Long getLatitude()
    {
        return latitude;
    }

    public DriverHeartbeatInfo() {
    }

    public DriverHeartbeatInfo(Long driverId, Long orderId) {
        this.driverId = driverId;
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("heartId", getHeartId())
            .append("driverId", getDriverId())
            .append("orderId", getOrderId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
