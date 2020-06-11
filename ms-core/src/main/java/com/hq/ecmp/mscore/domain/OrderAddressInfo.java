package com.hq.ecmp.mscore.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 order_address_info
 *
 * @author hqer
 * @date 2020-03-16
 */
@Data
public class OrderAddressInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long orderAddressId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
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
    private String userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityPostalCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date actionTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String addressLong;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double latitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String icaoCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String addressInfo;

    public OrderAddressInfo (){};

    public OrderAddressInfo(Long orderId) {
        this.orderId = orderId;
    }

    public OrderAddressInfo(String type, Long orderId){
    	this.orderId=orderId;
    	this.type=type;
    }

    public OrderAddressInfo(String type,Long orderId,Long journeyId){
        this.orderId=orderId;
        this.type=type;
        this.journeyId=journeyId;
    }

    public OrderAddressInfo(Long orderId, String type) {
        this.orderId = orderId;
        this.type = type;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderAddressId", getOrderAddressId())
            .append("orderId", getOrderId())
            .append("journeyId", getJourneyId())
            .append("nodeId", getNodeId())
            .append("powerId", getPowerId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .append("userId", getUserId())
            .append("cityPostalCode", getCityPostalCode())
            .append("actionTime", getActionTime())
            .append("address", getAddress())
            .append("addressLong", getAddressLong())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("icaoCode", getIcaoCode())
            .append("type", getType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("addressInfo", getAddressInfo())
            .toString();
    }
}
