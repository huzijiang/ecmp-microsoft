package com.hq.ecmp.constant;

import java.util.Arrays;
import java.util.List;

public enum OrderStateTrace {
    APPLYREASSIGNMENT("S270","司机申请改派"),
    
    TURNREASSIGNMENT("S277","改派请求驳回"),
    PASSREASSIGNMENT("S279","改派请求通过"),
    SENDCAR("S299","已派车"),
    PRESERVICE("S600","准备服务"),
    SERVICE("S616","服务中"),
    SERVICEOVER("S699","服务结束"),
    ORDERCLOSE("S900","订单关闭"),
    OBJECTION("S901","订单异议"),
    CANCEL("S911","订单取消")
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

   

}
