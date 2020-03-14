package com.hq.ecmp.mscore.domain;

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
 * 【请填写功能名称】对象 order_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long orderId;

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
    private String useCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualSetoutAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualSetoutLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualSetoutLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actualSetoutTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualArriveAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualArriveLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long actualArriveLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actualArriveTime;

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
    /**取消理由**/
    private String cancelReason;

    private String orderTraceState;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualArriveAddressLo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualSetoutAddressLo;

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
            .append("useCarMode", getUseCarMode())
            .append("state", getState())
            .append("actualSetoutAddress", getActualSetoutAddress())
            .append("actualSetoutLongitude", getActualSetoutLongitude())
            .append("actualSetoutLatitude", getActualSetoutLatitude())
            .append("actualSetoutTime", getActualSetoutTime())
            .append("actualArriveAddress", getActualArriveAddress())
            .append("actualArriveLongitude", getActualArriveLongitude())
            .append("actualArriveLatitude", getActualArriveLatitude())
            .append("actualArriveTime", getActualArriveTime())
            .append("tripartiteOrderId", getTripartiteOrderId())
            .append("tripartitePlatformCode", getTripartitePlatformCode())
            .append("driverName", getDriverName())
            .append("driverMobile", getDriverMobile())
            .append("carLicense", getCarLicense())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("cancelReason",getCancelReason())
            .append("actualArriveAddressLo",getActualArriveAddressLo())
            .append("actualSetoutAddressLo",getActualSetoutAddressLo())
            .toString();
    }
}
