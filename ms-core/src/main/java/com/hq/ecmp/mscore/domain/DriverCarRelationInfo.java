package com.hq.ecmp.mscore.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 driver_car_relation_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class DriverCarRelationInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;
    
    private List<Long> carIdList;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
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
    
    

    public List<Long> getCarIdList() {
		return carIdList;
	}

	public void setCarIdList(List<Long> carIdList) {
		this.carIdList = carIdList;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .toString();
    }
}
