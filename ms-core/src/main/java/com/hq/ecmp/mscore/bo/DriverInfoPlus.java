package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.DriverInfo;
import lombok.Data;

@Data
public class DriverInfoPlus {
    //司机信息
    private DriverInfo driverInfo;
    //完成订单数
    private int completeOrderCount;
    //在线数
    private int onLine;
    //在线时长
    private double onLineDuration;
}
