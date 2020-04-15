package com.hq.ecmp.mscore.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zj.hu
 * @Date: 2020-03-27 09:55
 */
@Data
public class SelectDriverConditionBo{

    /**
     * 调度员编号
     */
    private Long  dispatcherId;

    /**
     * 车队编号
     */
    private Long  carGroupId;

    /**
     * 城市代码
     */
    private String  cityCode;

    /**
     * 指定的司机编号
     */
    private Long  driverId;

    /**
     * 司机姓名或者电话
     */
    private String  driverNameOrPhone;

    /**
     * 上班日
     */
    private String    workDay;

    /**
     * 车辆编号
     */
    private String   carId;




}
