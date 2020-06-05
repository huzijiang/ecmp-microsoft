package com.hq.ecmp.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName
 * @Description 佛山后管调度列表状态枚举
 * @Author yj
 * @Date 2020/6/4 14:27
 * @Version 1.0
 */
public enum DispatchOrderStateTraceEnum {

    WAITINGLIST("S100","待派车"),
    ALREADYSENDING("S299","已处理"),
    REASSIGNMENT("S500","已处理"),
    READYSERVICE("S600","已处理"),
    INSERVICE("S616","已处理"),

    DRIVER_CONTINUED_SERVICE("S639","已处理"),
    SERVICE_SUSPEND("S635","已处理"),



    STOPSERVICE("S699","已处理"),
    ORDERCLOSE("S900","已处理"),
    ORDEROVERTIME("S921","已过期"),
    ORDERDENIED("S930","已驳回");
    private String state;

    private String stateName;

    DispatchOrderStateTraceEnum(String state,String stateName){
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

    public static DispatchOrderStateTraceEnum getStateEnum(String stateCode){
        DispatchOrderStateTraceEnum[] values = DispatchOrderStateTraceEnum.values();
        for (DispatchOrderStateTraceEnum dispatchOrderStateTraceEnum:
                values) {
            if(dispatchOrderStateTraceEnum.getState().equals(stateCode)){
                return dispatchOrderStateTraceEnum;
            }
        }
        return null;
    }

    /**
     * 已处理的订单状态集合
     * @return
     */
    public static List<String> dispatchStateAlreadyDeal(){
        return Arrays.asList(ALREADYSENDING.getState(),REASSIGNMENT.getState(),READYSERVICE.getState(),INSERVICE.getState(),
                DRIVER_CONTINUED_SERVICE.getState(),SERVICE_SUSPEND.getState(),STOPSERVICE.getState(),ORDERCLOSE.getState());
    }
}
