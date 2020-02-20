package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 invoice_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class InvoiceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long invoiceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long amount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String header;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tin;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bankName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bankCardNo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String registedAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String telephone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String acceptAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long applyer;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String status;

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId()
    {
        return invoiceId;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getHeader()
    {
        return header;
    }
    public void setTin(String tin)
    {
        this.tin = tin;
    }

    public String getTin()
    {
        return tin;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getBankName()
    {
        return bankName;
    }
    public void setBankCardNo(String bankCardNo)
    {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCardNo()
    {
        return bankCardNo;
    }
    public void setRegistedAddress(String registedAddress)
    {
        this.registedAddress = registedAddress;
    }

    public String getRegistedAddress()
    {
        return registedAddress;
    }
    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getTelephone()
    {
        return telephone;
    }
    public void setAcceptAddress(String acceptAddress)
    {
        this.acceptAddress = acceptAddress;
    }

    public String getAcceptAddress()
    {
        return acceptAddress;
    }
    public void setApplyer(Long applyer)
    {
        this.applyer = applyer;
    }

    public Long getApplyer()
    {
        return applyer;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("invoiceId", getInvoiceId())
            .append("amount", getAmount())
            .append("header", getHeader())
            .append("tin", getTin())
            .append("bankName", getBankName())
            .append("bankCardNo", getBankCardNo())
            .append("registedAddress", getRegistedAddress())
            .append("telephone", getTelephone())
            .append("acceptAddress", getAcceptAddress())
            .append("applyer", getApplyer())
            .append("content", getContent())
            .append("type", getType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
