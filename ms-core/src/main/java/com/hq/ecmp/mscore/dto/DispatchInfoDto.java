package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 调度信息 参数封装
 * @Author: zj.hu
 * @Date: 2020-03-17 22:36
 */
@Data
public class DispatchInfoDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    @ApiModelProperty(name = "dispatcherId", value = "调度员编号-当前是谁在操作")
    private String dispatcherId;

    /**
     * 申请单 编号
     */
    @ApiModelProperty(name = "applyId", value = "申请单 编号")
    private String applyId;


    /**
     * 订单 编号
     */
    @ApiModelProperty(name = "orderNo", value = "订单 编号")
    private String orderNo;


    /**
     * 车型级别代码
     * P001-公务级
     * P002-行政级
     * P003-六座商务
     */
    @ApiModelProperty(name = "carModelLevelType", value = "车型级别代码")
    private String carModelLevelType;

    /**
     * 车型名称
     */
    @ApiModelProperty(name = "carModelName", value = "车型名称")
    private String carModelName;


    /**
     * 车牌号
     */
    @ApiModelProperty(name = "plateLicence", value = "车牌号-调度员输入的内容")
    private String plateLicence;


    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carId", value = "车辆编号")
    private String carId;


    /**
     * 驾驶员名称
     */
    @ApiModelProperty(name = "driverName", value = "驾驶员名称-调度员输入的内容")
    private String driverName;


    /**
     * 驾驶员 电话信息
     */
    @ApiModelProperty(name = "driverPhone", value = "驾驶员-电话信息")
    private String driverPhone;

    /**
     * 驾驶员 编号
     */
    @ApiModelProperty(name = "driverId", value = "驾驶员 编号")
    private String driverId;



}
