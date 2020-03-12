package com.hq.ecmp.mscore.domain;

import lombok.Data;

/**
 * @ClassName OrderDriverList
 * @Description TODO
 * @Author yj
 * @Date 2020/3/6 12:41
 * @Version 1.0
 */
@Data
public class OrderDriverListInfo {
    //服务类型
    //1000预约
    //2001接机
    //2002送机
    //3000包车
    private String serviceType;
    //航班号
    private String flightNumber;
    //是否往返
    // Y000
    //N444
    private String itIsReturn;
    //包车类型：
    //T000  非包车
    //T001 半日租（4小时）
    //T002 整日租（8小时）
    private String charterCarType;
    //用车时间
    private String useCarTime;
    /**
     * 上车地址
     */
    private String startAddr;
    /**
     * 下车地址
     */
    private String endAddr;
    //订单状态
    private String state;

    //汽车id
    private Long carId;
    //司机ID
    private Long driverId;
    //订单id
    private Long orderId;

    //订单标签
    private String labelState;
    //订单类型(已完成,未完成)
    private String stateType;

}
