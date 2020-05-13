package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RunningOrderVo {
    @ApiModelProperty(name = "driverId",value = "订单id")
    private Long orderId;
    @ApiModelProperty(name = "state",value = "订单状态")
    private String state;
    @ApiModelProperty(name = "labelState",value = "辅助状态")
    private String labelState;
    @ApiModelProperty(name = "orderNumber",value = "订单编号")
    private String orderNumber;
    @ApiModelProperty(name = "useDate",value = "用车时间")
    private String useDate;
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
    @ApiModelProperty(name = "applyType",value = "申请单类型  公务/差旅")
    private String applyType;
    @ApiModelProperty(name = "canUseCarMode",value = "用车方式 W100自有车")
    private String canUseCarMode;
}
