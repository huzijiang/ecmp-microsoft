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
    ORDER_NOT_FIND_SET_OUT_ADDRESS("MDIS000002","订单未找到出发地址"),

    LOCK_CAR_NOT_EXIST("MDIS000003","锁定车辆时车辆未被发现"),
    LOCK_CAR_HAS_LOCKED("MDIS000004","锁定车辆时车辆已被锁定"),
    UNLOCK_CAR_NOT_EXIST("MDIS000005","解除锁定车辆时车辆未被发现"),
    LOCK_DRIVER_NOT_EXIST("MDIS000006","锁定司机时司机未被发现"),
    LOCK_DRIVER_HAS_LOCKED("MDIS000007","锁定司机时司机已被锁定"),
    UNLOCK_DRIVER_NOT_EXIST("MDIS000008","解除锁定司机时司机未被发现"),

    ORDER_NOT_FIND_PLAN_PRICE("MDIS000009","订单未找到预算价信息"),

    DISPATCHER_NOT_EXIST("MDIS000010","当前调度员不存在"),
    DISPATCHER_NOT_FIND_OWN_CAR_GROUP("MDIS000011","调度员没有找到对应的归属车队信息"),
    DISPATCHER_OWN_CAR_GROUP_SCOPE_IS_TOO_SMALL("MDIS000012","调度员归属的车队服务范围不足"),
    DRIVER_OR_CAR_NOT_FIND("MDIS000013","调度员没有找到对应的归属车队信息"),
    NOT_FIND_SUITABLE_CAR_SERVICE_SCOPE("MDIS000014","未找到匹配服务范围的车队"),

    NOT_FIND_THIS_CAR_OWN_CAR_GROUP_PRICE_PLAN_INFO("MDIS000015","未找到 该车辆归属车队的价格计划"),
    NOT_FIND_THIS_CAR_OWN_CAR_GROUP_PRICE_PLAN_INFO_WITH_SERVICE_MODE("MDIS000016","未找到 该车辆归属车队的价格计划（或当前价格计划中车队服务模式缺少:用车 或 车加人）"),
    NOT_FIND_THIS_CAR_OWN_CAR_GROUP_PRICE_PLAN_INFO_WITH_CAR_TYPE_LEVEL("MDIS000018","未找到 该车辆归属车队的价格计划(或当前价格计划中未发现当前车辆对应的车型级别)"),
    NOT_FIND_THIS_CAR_OWN_CAR_GROUP_PRICE_PLAN_INFO_WITH_RENT_TYPE("MDIS000019","未找到 该车辆归属车队的价格计划(或当前价格计划中缺少订单包车时长对应的配置：半日、整日、多日)"),

    NOT_FIND_THIS_DRIVER_OWN_CAR_GROUP_PRICE_PLAN_INFO("MDIS000020","未找到 该司机归属车队的价格计划"),
    NOT_FIND_THIS_DRIVER_OWN_CAR_GROUP_PRICE_PLAN_INFO_WITH_SERVICE_MODE("MDIS000021","未找到 该车辆归属车队的价格计划（或当前价格计划中车队服务模式缺少: 用人 或 车加人）"),
    NOT_FIND_THIS_DRIVER_OWN_CAR_GROUP_PRICE_PLAN_INFO_WITH_RENT_TYPE("MDIS000023","未找到 该车辆归属车队的价格计划(或当前价格计划中缺少订单包车时长对应的配置：半日、整日、多日)");

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
