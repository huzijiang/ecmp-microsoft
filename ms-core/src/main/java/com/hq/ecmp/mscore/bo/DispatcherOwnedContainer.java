package com.hq.ecmp.mscore.bo;

import java.util.LinkedHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 调度员 操作容器类  用于存储
 *
 *
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 21:38
 */
public class DispatcherOwnedContainer {

    /**
     *
     * key: String= 订单-调度员
     *
     *
     */
    public static LinkedHashMap<String, PriorityBlockingQueue<WaitSelectedCarBo>> waitSelectedCar;

    /**
     * key: String= 订单-调度员
     */
    public static LinkedHashMap<String, PriorityBlockingQueue<WaitSelectedDriverBo>> waitSelectedDriver;
}
