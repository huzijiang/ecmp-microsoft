package com.hq.ecmp.mscore.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
 * 【请填写功能名称】对象 journey_plan_price_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class JourneyPlanPriceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long priceId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long nodeId;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carTypeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long powerId;   //TODO 新增



    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal price;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date plannedDepartureTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer duration;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date plannedArrivalTime;


    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String source;  //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarMode;  //TODO 新增

    public JourneyPlanPriceInfo() {
    }

    public JourneyPlanPriceInfo(Long orderId) {
        this.orderId = orderId;
    }

    public JourneyPlanPriceInfo(Long journeyId, Long orderId, Long powerId) {
        this.journeyId = journeyId;
        this.orderId = orderId;
        this.powerId = powerId;
    }
    public JourneyPlanPriceInfo(Long journeyId, Long orderId) {
        this.journeyId = journeyId;
        this.orderId = orderId;
    }

    public Long getPowerId() {
		return powerId;
	}

	public void setPowerId(Long powerId) {
		this.powerId = powerId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("priceId", getPriceId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("carTypeId", getCarTypeId())
            .append("price", getPrice())
            .toString();
    }
}
