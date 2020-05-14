package com.hq.ecmp.mscore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单结算信息
 */
@Data
@ApiModel(description = "结算信息")
public class OrderSettlingInfoVo {


    /**
     * 结算信息主键
     */
    @ApiModelProperty(name = "billId", value = "结算信息主键")
    private Long billId;

    /**
     * 订单Id
     */
    @ApiModelProperty(name = "orderId", value = "订单Id")
    private Long orderId;

    /**
     * 订单总额
     */
    @ApiModelProperty(name = "amount", value = "订单总额")
    private BigDecimal amount;

    /**
     * 订单费用详细情况
     */
    @ApiModelProperty(name = "amountDetail", value = "订单费用详细情况")
    private String amountDetail;

    /**
     * 价外费
     */
    @ApiModelProperty(name = "outPrice", value = "价外费")
    private String outPrice;

    /**
     * 订单总里程
     */
    @ApiModelProperty(name = "totalMileage", value = "订单总里程")
    private BigDecimal totalMileage;

    /**
     * 订单总时长
     */
    @ApiModelProperty(name = "totalTime", value = "订单总时长")
    private Integer totalTime;

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
     * 等待时长
     */
    @ApiModelProperty(name = "waitingTime", value = "等待时长")
    private BigDecimal waitingTime;

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

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改者 */
    private String updateBy;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 发票照片
     */
    @ApiModelProperty(name = "imageUrl", value = "发票照片")
    private String imageUrl;
}
