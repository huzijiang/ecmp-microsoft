package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import lombok.Data;

@Data
public class CarInfoPlus {
    //车辆信息
    private CarInfo carInfo;
    //完成订单数
    private int useCar;
    //完成订单数
    private int completeOrderCount;
    //运行里程数
    private int mileageSum;
}
