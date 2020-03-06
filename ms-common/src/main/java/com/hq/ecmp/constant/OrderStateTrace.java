package com.hq.ecmp.constant;




public enum OrderStateTrace {
    APPLYREASSIGNMENT("S270","司机申请改派"),
    
    TURNREASSIGNMENT("S277","改派请求驳回"),
    PASSREASSIGNMENT("S279","改派请求通过")
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
