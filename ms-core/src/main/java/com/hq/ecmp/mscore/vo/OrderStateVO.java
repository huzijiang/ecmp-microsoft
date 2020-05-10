package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * 订单状态
 * @date: 2020/3/12 16:40
 * @author:caobj
 */

@Data
public class OrderStateVO {


    /**
     * 审批人Id
     */
    private Long orderId;
    private Long userId;
    private String state;
    private String labelState;
    private Long driverId;
    private Long journeyId;
    private String useCarMode;
    private String applyType;
    @ApiModelProperty(name = "startAddress",value = "出发地")
    private String startAddress;
    @ApiModelProperty(name = "endAddress",value = "目的地")
    private String endAddress;
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "charterCarType",value = "包车类型")
    private String charterCarType;
    @ApiModelProperty(name = "driverLongitude",value = "司机经度")
    private String driverLongitude;
    @ApiModelProperty(name = "driverLatitude",value = "司机纬度")
    private String driverLatitude;
    @ApiModelProperty(name = "endLatitude",value = "下车地纬度")
    private String endLatitude;//纬度
    @ApiModelProperty(name = "endLongitude",value = "下车地经度")
    private String endLongitude;//经度
    @ApiModelProperty(name = "startLatitude",value = "上车地纬度")
    private String startLatitude;//纬度
    @ApiModelProperty(name = "startLongitude",value = "上车地经度")
    private String startLongitude;//经度
    private String useCarTime;
    @ApiModelProperty(name = "planPrice",value = "预计价格")
    private String planPrice;
    @ApiModelProperty(name = "isDisagree",value = "是否展示异议")
    private int isDisagree;
    private String driverPhone;
    @ApiModelProperty(name = "excessMoney",value = "是否支付")
    private String payState;
}
