package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 car_maintenance_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarMaintenanceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long maintenanceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String maintenanceAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long price;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date nextMaintenanceDate;

    public void setMaintenanceId(Long maintenanceId)
    {
        this.maintenanceId = maintenanceId;
    }

    public Long getMaintenanceId()
    {
        return maintenanceId;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setMaintenanceAddress(String maintenanceAddress)
    {
        this.maintenanceAddress = maintenanceAddress;
    }

    public String getMaintenanceAddress()
    {
        return maintenanceAddress;
    }
    public void setPrice(Long price)
    {
        this.price = price;
    }

    public Long getPrice()
    {
        return price;
    }
    public void setNextMaintenanceDate(Date nextMaintenanceDate)
    {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Date getNextMaintenanceDate()
    {
        return nextMaintenanceDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("maintenanceId", getMaintenanceId())
            .append("carId", getCarId())
            .append("maintenanceAddress", getMaintenanceAddress())
            .append("price", getPrice())
            .append("nextMaintenanceDate", getNextMaintenanceDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
