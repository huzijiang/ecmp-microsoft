package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 order_invoice_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class OrderInvoiceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long invoiceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long accountId;

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId()
    {
        return invoiceId;
    }
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("invoiceId", getInvoiceId())
            .append("accountId", getAccountId())
            .toString();
    }
}
