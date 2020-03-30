package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:04
 */
@Data
public class DispatchSelectDriverDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    @ApiModelProperty(name = "dispatcherId", value = "调度员编号-当前是谁在操作",required = true)
    private String dispatcherId;

    /**
     * 申请单 编号
     */
    @ApiModelProperty(name = "applyId", value = "申请单 编号",required = true)
    private String applyId;

    /**
     * 订单 编号
     */
    @ApiModelProperty(name = "orderNo", value = "订单 编号",required = true)
    private String orderNo;

    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carId", value = "车辆编号")
    private String carId;


    /**
     * 驾驶员名称 或者 电话
     */
    @ApiModelProperty(name = "driverName", value = "驾驶员名称-调度员输入的内容")
    private String driverNameOrPhone;


    /**
     * 驾驶员 编号
     */
    @ApiModelProperty(name = "driverId", value = "驾驶员 编号,调度员选择的驾驶员编号")
    private String driverId;


}
