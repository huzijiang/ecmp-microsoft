package com.hq.ecmp.constant;

import java.util.Arrays;
import java.util.List;

public enum OrderStateTrace {
    APPLYREASSIGNMENT("S270","司机申请改派"),
    SENDINGCARS("S200","约车中"),
    TURNREASSIGNMENT("S277","改派请求驳回"),
    PASSREASSIGNMENT("S279","改派请求通过"),
    SENDCAR("S299","已派车"),
    PICKUPCAR("S380","已取车"),
    ALREADY_SET_OUT("S500","司机已出发"),
    PRESERVICE("S600","准备服务"),
    SERVICE("S616","服务中"),

    DRIVER_SERVICE_SUSPEND("S630","司机中止服务"),
    DRIVER_CONTINUED_SERVICE("S639","司机继续服务"),
    SERVICE_SUSPEND("S635","订单服务中止"),

    GIVE_UP_CAR("S680","还车"),

    SERVICEOVER("S699","服务结束"),
    ORDERCLOSE("S900","订单关闭"),
    OBJECTION("S901","订单异议"),
    CANCEL("S911","订单取消"),
    ORDEROVERTIME("S921","订单超时"),
    ORDERDENIED("S930","订单驳回"),
    CHANGINGCAR("S301","换车中"),
    APPLYINGPASS("S199","申请通过中"),
    TRAVELOVERUSECARTIME("S980","差旅权限超过用车时间，仍然可约车的状态"),
    ORDERDENYNOUSE("S931","订单驳回，后面的用车权限有已完成的，此为驳回不可用的情况状态码"),
    TRAVELOVERUSECARTIMENOUSE("S990","后面与已经完成的权限，前面未使用的权限变为已过期对应的状态码")
    ;


    private String state;

    private String stateName;

    OrderStateTrace(String state,String stateName){
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

    public static List<String> getNetCarHave() {
        return Arrays.asList(APPLYREASSIGNMENT.getState(), TURNREASSIGNMENT.getState());
    }
    
    public static List<String> getCancelAndOverTime(){
    	return Arrays.asList(CANCEL.getState(),ORDEROVERTIME.getState(),ORDERDENIED.getState());
    }

    public static String manyUseCarState() {
        return DRIVER_SERVICE_SUSPEND.getState()+","+DRIVER_CONTINUED_SERVICE.getState()+","+SERVICE_SUSPEND.getState();
    }

    public static OrderStateTrace getStateEnum(String stateCode){
        OrderStateTrace[] values = OrderStateTrace.values();
        for (OrderStateTrace orderStateTrace:
             values) {
            if(orderStateTrace.getState().equals(stateCode)){
                return orderStateTrace;
            }
        }
        return null;
    }


}
