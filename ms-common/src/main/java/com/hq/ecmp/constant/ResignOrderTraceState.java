package com.hq.ecmp.constant;

/**
 * @ClassName ResignOrderTraceState
 * @Description TODO
 * @Author yj
 * @Date 2020/3/5 17:42
 * @Version 1.0
 */
public enum  ResignOrderTraceState {

    APPLYING("S270","司机申请改派"),DISAGREE("S277","改派请求驳回"),
    AGREE("S279","改派请求通过");

    private String state;

    private String stateName;

    ResignOrderTraceState(String state,String stateName){
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
