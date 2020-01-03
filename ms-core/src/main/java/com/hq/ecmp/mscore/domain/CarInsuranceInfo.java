package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 car_insurance_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarInsuranceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long carInsuranceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date insuranceBeginDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date insuranceEndDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long price;

    public void setCarInsuranceId(Long carInsuranceId)
    {
        this.carInsuranceId = carInsuranceId;
    }

    public Long getCarInsuranceId()
    {
        return carInsuranceId;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setInsuranceBeginDate(Date insuranceBeginDate)
    {
        this.insuranceBeginDate = insuranceBeginDate;
    }

    public Date getInsuranceBeginDate()
    {
        return insuranceBeginDate;
    }
    public void setInsuranceEndDate(Date insuranceEndDate)
    {
        this.insuranceEndDate = insuranceEndDate;
    }

    public Date getInsuranceEndDate()
    {
        return insuranceEndDate;
    }
    public void setPrice(Long price)
    {
        this.price = price;
    }

    public Long getPrice()
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carInsuranceId", getCarInsuranceId())
            .append("carId", getCarId())
            .append("insuranceBeginDate", getInsuranceBeginDate())
            .append("insuranceEndDate", getInsuranceEndDate())
            .append("price", getPrice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
