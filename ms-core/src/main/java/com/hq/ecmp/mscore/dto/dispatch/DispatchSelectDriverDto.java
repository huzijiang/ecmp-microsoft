package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:04
 */
@Data
public class DispatchSelectDriverDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    private String dispatcherId;
    /**
     * 订单 编号
     */
    private String orderNo;

    /**
     * 车辆编号
     */
    private String carId;

    /**
     * 驾驶员名称 或者 电话
     */
    private String driverNameOrPhone;


}
