package com.hq.ecmp.mscore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 派车订单信息对象 order_info
 *
 * @author hqer
 * @date 2020-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long orderId;

    private Long companyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long nodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long powerId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tripartiteOrderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tripartitePlatformCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverMobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicense;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String flightNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String demandCarLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String orderTraceState;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flightPlanTakeOffTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String orderNumber;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carModel;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carColor;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverGrade;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String labelState;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerCompany;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsSupplement;

    public OrderInfo(Long orderId, String state) {
        this.orderId = orderId;
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("powerId", getPowerId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .append("userId", getUserId())
            .append("useCarMode", getUseCarMode())
            .append("serviceType", getServiceType())
            .append("state", getState())
            .append("tripartiteOrderId", getTripartiteOrderId())
            .append("tripartitePlatformCode", getTripartitePlatformCode())
            .append("driverName", getDriverName())
            .append("driverMobile", getDriverMobile())
            .append("carLicense", getCarLicense())
            .append("flightNumber", getFlightNumber())
            .append("demandCarLevel", getDemandCarLevel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("orderNumber",getOrderNumber())
            .append("labelState",getLabelState())
            .append("ownerCompany",getOwnerCompany())
            .append("itIsSupplement",getItIsSupplement())
            .toString();
    }
}
