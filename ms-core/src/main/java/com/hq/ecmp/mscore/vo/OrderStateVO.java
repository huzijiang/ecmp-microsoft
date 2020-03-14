package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private String state;
    private String labelState;
    private Long driverId;
    private String useCarMode;
    @ApiModelProperty(name = "startAddress",value = "出发地")
    private String startAddress;
    @ApiModelProperty(name = "endAddress",value = "目的地")
    private String endAddress;
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
}
