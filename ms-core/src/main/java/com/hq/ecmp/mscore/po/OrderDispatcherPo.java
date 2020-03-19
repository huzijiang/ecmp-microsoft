package com.hq.ecmp.mscore.po;

import java.math.BigInteger;

/**
 * @Author: zj.hu
 * @Date: 2020-03-18 00:09
 */
public class OrderDispatcherPo {

    /**
     * 订单指定的司机编号
     */
    private BigInteger driverId;

    /**
     *订单出发城市 编码
     */
    private String  city;

    /**
     * 订单要求车辆级别
     */
    private String  level;

    /**
     *订单载客数量要求
     */
    private String  passengersNumber;



}
