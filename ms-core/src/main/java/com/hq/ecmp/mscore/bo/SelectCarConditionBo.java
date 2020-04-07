package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-21 10:47
 */
@Data
public class SelectCarConditionBo {

    /**
     * 调度员编号
     */
    private String dispatcherId;

    /**
     * 车队编号
     */
    private String carGroupId;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 乘客数量
     */
    private int   passengers;


    /**
     * 车辆级别要求
     */
    private String carLevel;

    /**
     * 指定的司机编号
     */
    private String  driverId;

    /**
     * 车牌号
     */
    private String  carLicense;

    /**
     * 车型车系信息
     */
    private String  carTypeInfo;

}
