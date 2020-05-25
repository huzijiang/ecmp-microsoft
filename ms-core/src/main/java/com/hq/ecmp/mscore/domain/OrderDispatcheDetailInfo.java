package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 order_dispatche_detail_info
 * 
 * @author hqer
 * @date 2020-05-25
 */
public class OrderDispatcheDetailInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer dispatchId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsUseInnerCarGroup;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carGroupUserMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsSelfDriver;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String nextCarGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String charterCarType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carCgId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverCgId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String dispatchState;

    public void setDispatchId(Integer dispatchId) 
    {
        this.dispatchId = dispatchId;
    }

    public Integer getDispatchId() 
    {
        return dispatchId;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setItIsUseInnerCarGroup(String itIsUseInnerCarGroup) 
    {
        this.itIsUseInnerCarGroup = itIsUseInnerCarGroup;
    }

    public String getItIsUseInnerCarGroup() 
    {
        return itIsUseInnerCarGroup;
    }
    public void setCarGroupUserMode(String carGroupUserMode) 
    {
        this.carGroupUserMode = carGroupUserMode;
    }

    public String getCarGroupUserMode() 
    {
        return carGroupUserMode;
    }
    public void setItIsSelfDriver(String itIsSelfDriver) 
    {
        this.itIsSelfDriver = itIsSelfDriver;
    }

    public String getItIsSelfDriver() 
    {
        return itIsSelfDriver;
    }
    public void setNextCarGroupId(String nextCarGroupId) 
    {
        this.nextCarGroupId = nextCarGroupId;
    }

    public String getNextCarGroupId() 
    {
        return nextCarGroupId;
    }
    public void setCharterCarType(String charterCarType) 
    {
        this.charterCarType = charterCarType;
    }

    public String getCharterCarType() 
    {
        return charterCarType;
    }
    public void setCarId(Long carId) 
    {
        this.carId = carId;
    }

    public Long getCarId() 
    {
        return carId;
    }
    public void setCarCgId(Long carCgId) 
    {
        this.carCgId = carCgId;
    }

    public Long getCarCgId() 
    {
        return carCgId;
    }
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setDriverCgId(Long driverCgId) 
    {
        this.driverCgId = driverCgId;
    }

    public Long getDriverCgId() 
    {
        return driverCgId;
    }
    public void setDispatchState(String dispatchState) 
    {
        this.dispatchState = dispatchState;
    }

    public String getDispatchState() 
    {
        return dispatchState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dispatchId", getDispatchId())
            .append("orderId", getOrderId())
            .append("itIsUseInnerCarGroup", getItIsUseInnerCarGroup())
            .append("carGroupUserMode", getCarGroupUserMode())
            .append("itIsSelfDriver", getItIsSelfDriver())
            .append("nextCarGroupId", getNextCarGroupId())
            .append("charterCarType", getCharterCarType())
            .append("carId", getCarId())
            .append("carCgId", getCarCgId())
            .append("driverId", getDriverId())
            .append("driverCgId", getDriverCgId())
            .append("dispatchState", getDispatchState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
