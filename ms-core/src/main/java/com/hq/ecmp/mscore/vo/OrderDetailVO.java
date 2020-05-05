package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderDetailVO {
    @ApiModelProperty(name = "driverId",value = "订单id")
    private Long orderId;
    @ApiModelProperty(name = "state",value = "订单状态")
    private String state;
    @ApiModelProperty(name = "labelState",value = "辅助状态")
    private String labelState;
    @ApiModelProperty(name = "orderNumber",value = "订单编号")
    private String orderNumber;
    @ApiModelProperty(name = "orderStartTime",value = "开始时间")
    private String orderStartTime;
    @ApiModelProperty(name = "orderEndTime",value = "结束时间")
    private String orderEndTime;
    @ApiModelProperty(name = "startAddress",value = "出发地")
    private String startAddress;
    @ApiModelProperty(name = "endAddress",value = "目的地")
    private String endAddress;
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "charterCarType",value = "包车类型")
    private String charterCarType;
    @ApiModelProperty(name = "passengerName",value = "乘车人")
    private String passengerName;
    @ApiModelProperty(name = "passengerPhone",value = "乘车人手机")
    private String passengerPhone;
    @ApiModelProperty(name = "carColor",value = "车辆颜色")
    private String carColor;
    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;
    @ApiModelProperty(name = "carGrade",value = "车辆级别")
    private String carGrade;
    @ApiModelProperty(name = "carType",value = "车辆类型")
    private String carType;
}
