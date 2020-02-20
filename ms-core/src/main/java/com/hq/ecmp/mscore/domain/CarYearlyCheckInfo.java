package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_yearly_check_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarYearlyCheckInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long yearlyCheckId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String yearCheckNextDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String yearCheckState;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String result;

    public void setYearlyCheckId(Long yearlyCheckId)
    {
        this.yearlyCheckId = yearlyCheckId;
    }

    public Long getYearlyCheckId()
    {
        return yearlyCheckId;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setYearCheckNextDate(String yearCheckNextDate)
    {
        this.yearCheckNextDate = yearCheckNextDate;
    }

    public String getYearCheckNextDate()
    {
        return yearCheckNextDate;
    }
    public void setYearCheckState(String yearCheckState)
    {
        this.yearCheckState = yearCheckState;
    }

    public String getYearCheckState()
    {
        return yearCheckState;
    }
    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("yearlyCheckId", getYearlyCheckId())
            .append("carId", getCarId())
            .append("yearCheckNextDate", getYearCheckNextDate())
            .append("yearCheckState", getYearCheckState())
            .append("result", getResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
