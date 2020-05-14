package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderInfoPlus {
    //司机信息
    private OrderInfo orderInfo;
    //派单量
    private int dispatchCount;
    //派单耗时
    private int dispatchDuration;
    //完单量
    private int orderCloseCount;
    //订单里程
    private int Mileage;
    //订单耗时
    private double OrderDuration;
}
