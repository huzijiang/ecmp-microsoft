package com.hq.ecmp.mscore.bo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 * 调度员 操作容器类  用于临时存储待调度的车  和  驾驶员
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 21:38
 */
@Data
public class DispatcherOwnedContainer {

    /**
     *
     * key: String= 订单-调度员
     * PriorityBlockingQueue<WaitSelectedCarBo>  待调度的车辆
     *
     */
    public static LinkedHashMap<String, PriorityBlockingQueue<WaitSelectedCarBo>> waitSelectedCars;

    /**
     * key: String= 订单-调度员
     *
     * PriorityBlockingQueue<WaitSelectedCarBo>  待调度的车辆 待调度的司机
     */
    public static LinkedHashMap<String, PriorityBlockingQueue<WaitSelectedDriverBo>> waitSelectedDrivers;


}
