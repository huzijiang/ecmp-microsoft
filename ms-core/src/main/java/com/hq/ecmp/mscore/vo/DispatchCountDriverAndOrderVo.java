package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;

/**
 * @Author: zj.hu
 * @Date: 2020-03-23 20:55
 */
@Data
public class DispatchCountDriverAndOrderVo {
    /**
     * 可选车辆列表,
     */
    @ApiModelProperty(name="carList",value="可选车辆列表")
    private LinkedList<CarInfoVO> carList;

    /**
     * 可选司机列表
     */
    @ApiModelProperty(name="driverList",value="可选司机列表")
    private LinkedList<DriverInfoVO> driverList;

    @ApiModelProperty(name="carAmount",value="车辆可选数量")
    private int carAmount;
    @ApiModelProperty(name="driverAmount",value="司机可选数量")
    private int driverAmount;
}
