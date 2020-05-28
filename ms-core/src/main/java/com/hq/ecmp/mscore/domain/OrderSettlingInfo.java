package com.hq.ecmp.mscore.domain;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 【请填写功能名称】对象 order_settling_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
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
    private BigDecimal amount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String amountDetail;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String outPrice;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal totalMileage;// TODO 新增。实际里程
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal totalTime;// TODO 新增。实际时长

    public OrderSettlingInfo() {
    }

    public OrderSettlingInfo(Long orderId, BigDecimal amount, String amountDetail, BigDecimal totalMileage, BigDecimal totalTime) {
        this.orderId = orderId;
        this.amount = amount;
        this.amountDetail = amountDetail;
        this.totalMileage = totalMileage;
        this.totalTime = totalTime;
    }

    public OrderSettlingInfo(Long orderId) {
        this.orderId = orderId;
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
