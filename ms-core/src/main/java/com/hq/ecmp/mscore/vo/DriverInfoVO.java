package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
public class DriverInfoVO {

    /**
     * 司机ID
     */
    private Integer driverId;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 司机手机
     */
    private String driverPhone;
    /**
     * 状态
     * 符合、任务冲突、无可用车辆
     */
    private String status;

    /**
     * 车队电话
     */
    private String fleetPhone;
}
