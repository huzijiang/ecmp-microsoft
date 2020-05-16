package com.hq.ecmp.constant.enumerate;

/**
*   @author yj
*   @Description  //TODO 调度前端状态类
*   @Date 9:47 2020/5/12
*   @Param
*   @return
**/
public enum DispatcherFrontState {


    REASSIGNMENTHAVE("D001","自有车已改派"),
    REASSIGNMENTNET("D002","网约车已改派"),
    REASSIGNMENTREJECT("D003","改派申请已驳回"),
    SENDCARREJECT("D004","派车申请已驳回"),
    SENDCARHAVE("D005","自有车已派车"),
    SENDCARNET("D006","网约车已派车"),
    REASSIGNMENTEXPIRED("D007","改派已过期"),
    SENDCAREXPIRED("D008","派车已过期");
    private String state;

    private String stateName;

     DispatcherFrontState(String state,String stateName){
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
