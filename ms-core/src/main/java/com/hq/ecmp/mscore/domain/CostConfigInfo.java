package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 cost_config_info
 * 
 * @author hqer
 * @date 2020-05-06
 */
public class CostConfigInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long costId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long companyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String costConfigName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String rentType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double startPrice;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double combosPrice;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double combosMileage;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long combosTimes;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double beyondPriceEveryKm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double beyondPriceEveryMinute;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double waitPriceEreryMinute;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    public void setCostId(Long costId) 
    {
        this.costId = costId;
    }

    public Long getCostId() 
    {
        return costId;
    }
    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }
    public void setCostConfigName(String costConfigName) 
    {
        this.costConfigName = costConfigName;
    }

    public String getCostConfigName() 
    {
        return costConfigName;
    }
    public void setServiceType(String serviceType) 
    {
        this.serviceType = serviceType;
    }

    public String getServiceType() 
    {
        return serviceType;
    }
    public void setRentType(String rentType) 
    {
        this.rentType = rentType;
    }

    public String getRentType() 
    {
        return rentType;
    }
    public void setStartPrice(Double startPrice) 
    {
        this.startPrice = startPrice;
    }

    public Double getStartPrice() 
    {
        return startPrice;
    }
    public void setCombosPrice(Double combosPrice) 
    {
        this.combosPrice = combosPrice;
    }

    public Double getCombosPrice() 
    {
        return combosPrice;
    }
    public void setCombosMileage(Double combosMileage) 
    {
        this.combosMileage = combosMileage;
    }

    public Double getCombosMileage() 
    {
        return combosMileage;
    }
    public void setCombosTimes(Long combosTimes) 
    {
        this.combosTimes = combosTimes;
    }

    public Long getCombosTimes() 
    {
        return combosTimes;
    }
    public void setBeyondPriceEveryKm(Double beyondPriceEveryKm) 
    {
        this.beyondPriceEveryKm = beyondPriceEveryKm;
    }

    public Double getBeyondPriceEveryKm() 
    {
        return beyondPriceEveryKm;
    }
    public void setBeyondPriceEveryMinute(Double beyondPriceEveryMinute) 
    {
        this.beyondPriceEveryMinute = beyondPriceEveryMinute;
    }

    public Double getBeyondPriceEveryMinute() 
    {
        return beyondPriceEveryMinute;
    }
    public void setWaitPriceEreryMinute(Double waitPriceEreryMinute) 
    {
        this.waitPriceEreryMinute = waitPriceEreryMinute;
    }

    public Double getWaitPriceEreryMinute() 
    {
        return waitPriceEreryMinute;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("costId", getCostId())
            .append("companyId", getCompanyId())
            .append("costConfigName", getCostConfigName())
            .append("serviceType", getServiceType())
            .append("rentType", getRentType())
            .append("startPrice", getStartPrice())
            .append("combosPrice", getCombosPrice())
            .append("combosMileage", getCombosMileage())
            .append("combosTimes", getCombosTimes())
            .append("beyondPriceEveryKm", getBeyondPriceEveryKm())
            .append("beyondPriceEveryMinute", getBeyondPriceEveryMinute())
            .append("waitPriceEreryMinute", getWaitPriceEreryMinute())
            .append("state", getState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}