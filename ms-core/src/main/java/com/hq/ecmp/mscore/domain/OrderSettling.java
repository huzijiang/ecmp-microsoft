package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单结算信息
 */
@Data
@ApiModel(description = "结算信息JSON")
public class OrderSettling {
    /**
     * 路桥费
     */
    @ApiModelProperty(name = "roadBridgeFee", value = "路桥费")
    private BigDecimal roadBridgeFee;

    /**
     * 高速费
     */
    @ApiModelProperty(name = "highSpeedFee", value = "高速费")
    private BigDecimal  highSpeedFee;

    /**
     * 停车费
     */
    @ApiModelProperty(name = "parkingRateFee", value = "停车费")
    private BigDecimal parkingRateFee;

    /**
     * 住宿费
     */
    @ApiModelProperty(name = "hotelExpenseFee", value = "住宿费")
    private BigDecimal hotelExpenseFee;

    /**
     * 餐饮费
     */
    @ApiModelProperty(name = "restaurantFee", value = "餐饮费")
    private BigDecimal restaurantFee;

    /**
     * 起步价
     */
    @ApiModelProperty(name = "startingPrice", value = "起步价")
    private BigDecimal startingPrice;

    /**
     * 超里程价格
     */
    @ApiModelProperty(name = "overMileagePrice", value = "超里程价格")
    private BigDecimal overMileagePrice;

    /**
     * 超时长价格
     */
    @ApiModelProperty(name = "overtimeLongPrice", value = "超时长价格")
    private BigDecimal overtimeLongPrice;

    /**
     * 等待费
     */
    @ApiModelProperty(name = "waitingFee", value = "等待费")
    private BigDecimal waitingFee;

    /**
     * 取消费
     */
    @ApiModelProperty(name = " cancellationFee ", value = "取消费")
    private BigDecimal cancellationFee;
}
