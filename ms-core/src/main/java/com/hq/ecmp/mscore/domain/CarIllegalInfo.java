package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_illegal_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarIllegalInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long illegalId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String score;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverId;

    public void setIllegalId(Long illegalId)
    {
        this.illegalId = illegalId;
    }

    public Long getIllegalId()
    {
        return illegalId;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setScore(String score)
    {
        this.score = score;
    }

    public String getScore()
    {
        return score;
    }
    public void setDriverId(String driverId)
    {
        this.driverId = driverId;
    }

    public String getDriverId()
    {
        return driverId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("illegalId", getIllegalId())
            .append("carId", getCarId())
            .append("address", getAddress())
            .append("score", getScore())
            .append("driverId", getDriverId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
