package com.hq.ecmp.constant;


import java.util.Arrays;
import java.util.List;

public enum OrderState {
    INITIALIZING("S000","订单已生成"),WAITINGLIST("S100","待派单"),
    GETARIDE("S101","去约车"),SENDINGCARS("S200","约车中"),
    REASSIGNMENT("S201","待改派"),
    ALREADYSENDING("S299","已派单"),
    ALREADY_SET_OUT("S500","司机已出发"),
    READYSERVICE("S600","准备服务"),INSERVICE("S616","服务中"),
    STOPSERVICE("S699","停止服务"),ORDERCLOSE("S900","订单关闭");


    private String state;

    private String stateName;

    OrderState(String state,String stateName){
        this.state = state;
        this.stateName = stateName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    //已找到车未服务的状态集合
    public static List<String> getNoAppointmentCar() {
        return Arrays.asList(ALREADYSENDING.getState(), READYSERVICE.getState());
    }

    //未找到车的状态
    public static List<String> getContractedCar() {
        return Arrays.asList(INITIALIZING.getState(), WAITINGLIST.getState(),GETARIDE.getState(),SENDINGCARS.getState());
    }

}
