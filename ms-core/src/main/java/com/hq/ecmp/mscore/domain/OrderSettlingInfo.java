package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 order_settling_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class OrderSettlingInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long billId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long amount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String amountDetail;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long outPrice;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long totalMileage;// TODO 新增。实际里程
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String totalTime;// TODO 新增。实际时长
    public void setTotalMileage(Long totalMileage)
    {
        this.totalMileage = totalMileage;
    }
    public Long getTotalMileage()
    {
        return totalMileage;
    }

    public void setTotalTime(String totalTime)
    {
        this.totalTime = totalTime;
    }
    public String getTotalTime()
    {
        return totalTime;
    }

    public void setBillId(Long billId)
    {
        this.billId = billId;
    }
    public Long getBillId()
    {
        return billId;
    }
    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setAmountDetail(String amountDetail)
    {
        this.amountDetail = amountDetail;
    }

    public String getAmountDetail()
    {
        return amountDetail;
    }
    public void setOutPrice(Long outPrice)
    {
        this.outPrice = outPrice;
    }

    public Long getOutPrice()
    {
        return outPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("billId", getBillId())
            .append("orderId", getOrderId())
            .append("amount", getAmount())
            .append("amountDetail", getAmountDetail())
            .append("outPrice", getOutPrice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("totalMileage", getTotalMileage())
            .append("totalTime", getTotalTime())
            .toString();
    }
}
