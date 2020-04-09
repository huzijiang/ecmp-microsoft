package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 调度异常信息枚举
 * @Author: zj.hu
 * @Date: 2020-04-09 17:58
 */
public enum DispatchExceptionEnum {

    ORDER_NOT_EXIST("MDIS000001","订单不存在"),
    ORDER_NOT_FIND_SET_OUT_ADDRESS("MDIS000001","订单未找到出发地址"),
    LOCK_CAR_NOT_EXIST("MDIS000001","锁定车辆时车辆未被发现"),
    LOCK_CAR_HAS_LOCKED("MDIS000001","锁定车辆时车辆已被锁定"),
    UNLOCK_CAR_NOT_EXIST("MDIS000001","解除锁定车辆时车辆未被发现"),
    LOCK_DRIVER_NOT_EXIST("MDIS000001","锁定司机时司机未被发现"),
    LOCK_DRIVER_HAS_LOCKED("MDIS000001","锁定司机时司机已被锁定"),
    UNLOCK_DRIVER_NOT_EXIST("MDIS000001","解除锁定司机时司机未被发现"),
    ORDER_NOT_FIND_PLAN_PRICE("MDIS000001","订单未找到预算价信息"),
    DISPATCHER_NOT_ExIST("MDIS000001","当前调度员不存在"),
    DISPATCHER_NOT_FIND_OWN_CAR_GROUP("MDIS000001","调度员没有找到对应的归属车队信息"),
    DISPATCHER_OWN_CAR_GROUP_SCOPE_IS_TOO_SMALL("MDIS000001","调度员归属的车队服务范围不足");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    DispatchExceptionEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
