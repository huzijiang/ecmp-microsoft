package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:11
 */
@Data
public class DispatchLockDriverDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    private String dispatcherId;
    /**
     * 申请单 编号
     */
    private String applyId;
    /**
     * 订单 编号
     */
    private String orderNo;

    /**
     * 车辆编号
     */
    private String carId;

    /**
     * 驾驶员 编号
     */
    private String driverId;
}
