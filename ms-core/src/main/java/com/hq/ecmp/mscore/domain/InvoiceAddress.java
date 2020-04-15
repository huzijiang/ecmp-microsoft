package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 invoice_address
 *
 * @author hqer
 * @date 2020-01-02
 */
public class InvoiceAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long addressId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String accepter;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String mobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsDefault;

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
    }

    public Long getAddressId()
    {
        return addressId;
    }
    public void setAccepter(String accepter)
    {
        this.accepter = accepter;
    }

    public String getAccepter()
    {
        return accepter;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setItIsDefault(String itIsDefault)
    {
        this.itIsDefault = itIsDefault;
    }

    public String getItIsDefault()
    {
        return itIsDefault;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("addressId", getAddressId())
            .append("accepter", getAccepter())
            .append("mobile", getMobile())
            .append("address", getAddress())
            .append("itIsDefault", getItIsDefault())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
