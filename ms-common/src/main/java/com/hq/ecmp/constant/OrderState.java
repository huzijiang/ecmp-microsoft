package com.hq.ecmp.constant;


import java.util.Arrays;
import java.util.List;

public enum OrderState {
    INITIALIZING("S000","订单已生成"),WAITINGLIST("S100","待派单"),
    GETARIDE("S101","去约车"),SENDINGCARS("S200","约车中"),
    APPLYREASSIGN("S270","司机申请改派"),REASSIGNREJECT("S277","改派驳回"),
    REASSIGNPASS("S279","改派通过"),
    ALREADYSENDING("S299","已派车"),REASSIGNMENT("S500","前往出发地"),
    READYSERVICE("S600","准备服务"),INSERVICE("S616","服务中"),
    STOPSERVICE("S699","服务结束"),ORDERCLOSE("S900","订单关闭"),
    DISSENT("S901","订单异议"),ORDERCANCEL("S911","订单取消"),
    ORDEROVERTIME("S921","订单超时");


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
