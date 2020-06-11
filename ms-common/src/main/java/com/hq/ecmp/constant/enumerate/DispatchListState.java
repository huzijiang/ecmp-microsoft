package com.hq.ecmp.constant.enumerate;

import com.hq.ecmp.constant.OrderState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>调度工作台状态 --待派车 -- 已派车 --待出车 --待还车  --可用车辆  --在外车辆</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 15:56
 */
public enum  DispatchListState {
    waitSendCar("待派车"),
    alreadySendCar("已派车"),
    waitUseCar("待出车"),
    waitReturnCar("待还车"),
    idleCars("可用车辆"),
    busyCars("在外车辆");

    private String stateName;

    DispatchListState(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    /**
     * 调度单状态纬度
     * @return
     */
    public static List<String> getOrderState(){
        return Arrays.asList(new String[]{waitSendCar.getStateName(),alreadySendCar.getStateName(),waitUseCar.getStateName(),waitReturnCar.getStateName()});
    }

    /**
     * 车辆纬度
     * @return
     */
    public static List<String> getCarState(){
        return Arrays.asList(new String[]{idleCars.getStateName(),busyCars.getStateName()});
    }

}
