package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;

/**
 *
 * 调度结果 视图对象
 * @Author: zj.hu
 * @Date: 2020-03-16 01:20
 */
@Data
public class DispatchResultVo {

    /**
     * 可选车辆列表
     */
    @ApiModelProperty(name="carList",value="可选车辆列表")
    private LinkedList<CarInfoVO> carList;

    /**
     * 可选司机列表
     */
    @ApiModelProperty(name="driverList",value="可选司机列表")
    private LinkedList<DriverInfoVO> driverList;

    /**
     * 是否有车辆
     * 0 有
     * 1 无
     * 2 未启动查询
     */
    @ApiModelProperty(name="hasCar",value="是否有车辆 0-有  1-无  2 未启动查询")
    private int  hasCar;


    /**
     *
     * 是否有司机
     * 0 有
     * 1 无
     * 2 未启动查询
     */
    @ApiModelProperty(name="hasDriver",value="是否有司机  0-有  1-无 2 未启动查询")
    private int  hasDriver;

    /**
     * 锁定车辆结果，可能会失败
     * 0 锁定车辆成功
     * 1 锁定车辆失败
     * 2 未执行操作
     */
    @ApiModelProperty(name="selectedDriverHasCar",value="锁定车辆结果，可能会失败 0-成功  1-失败 2-未执行操作")
    private int  lockCar;

    /**
     * 锁定司机结果，可能会失败
     * 0 锁定司机成功
     * 1 锁定司机失败
     * 2 未执行操作
     */
    @ApiModelProperty(name="selectedCarHasDriver",value="锁定司机结果，可能会失败 0-成功  1-失败 2-未执行操作")
    private int  lockDriver;

}
