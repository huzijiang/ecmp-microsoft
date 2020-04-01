package com.hq.ecmp.mscore.bo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 *车辆任务是否冲突数据封装类
 *
 * @Author: zj.hu
 * @Date: 2020-03-23 16:10
 */

@Data
public class OrderTaskClashBo {

    /**
     * 司机编号
     */
    private Long driverId;

    /**
     * 司机姓名或者电话
     */
    private String  driverNameOrPhone;

    /**
     * 车辆编号
     */
    private Long carId;



    /**
     * 车牌号
     */
    private String carLicense;

    /**
     * 出发时间 已经算上了缓冲时间
     */
    private Date  setOutTime;

    /**
     * 到达时间  已经算上车辆缓冲时间
     */
    private Date arrivalTime;


}
