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
    WAITCONFIRMED("S960","待确认"),
    TIMELIMIT("S970","已过期"),
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
        return Arrays.asList(ALREADYSENDING.getState(), READYSERVICE.getState(),REASSIGNMENT.getState());
    }
    //网约车已经约到车的状态
    public static List<String> getNetCarHave() {
        return Arrays.asList(ALREADYSENDING.getState(), READYSERVICE.getState(),REASSIGNMENT.getState(),INSERVICE.getState(),STOPSERVICE.getState(),ORDERCLOSE.getState());
    }

    //未找到车的状态
    public static List<String> getContractedCar() {
        return Arrays.asList(INITIALIZING.getState(), WAITINGLIST.getState(),GETARIDE.getState(),SENDINGCARS.getState());
    }

    
    public static List<String> getSendCar() {
        return Arrays.asList(ALREADYSENDING.getState(), REASSIGNMENT.getState(),READYSERVICE.getState(),INSERVICE.getState(),STOPSERVICE.getState(),ORDERCLOSE.getState());
    }
    
    //派车中/约车中
    public static List<String> getWaitSendCar() {
        return Arrays.asList(WAITINGLIST.getState(), SENDINGCARS.getState());
    }
    //去申请/去约车
    public static List<String> getApplyState() {
        return Arrays.asList(INITIALIZING.getState(), GETARIDE.getState());
    }


    /**
     * 用车权限用来判断订单是否完成 --S699,S900,S901
     * @return
     */
    public static List<String> carAuthorityJundgeOrderComplete() {
        return Arrays.asList(STOPSERVICE.getState(),ORDERCLOSE.getState(),DISSENT.getState());
    }
    /**
     * 用车权限标识订单完成的前端状态
     * 待确认和订单关闭（订单异议对应前端的订单关闭）
     */
    public static List<String> carAuthorityJundgeOrderCompleteFront() {
        return Arrays.asList(STOPSERVICE.getState(),WAITCONFIRMED.getState());
    }
}
