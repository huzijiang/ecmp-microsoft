package com.hq.ecmp.constant;

public enum OrderServiceType {

    //订单下单类型
    ORDER_SERVICE_TYPE_NOW("1","1000","即时用车，目前和预约一样"),
    ORDER_SERVICE_TYPE_APPOINTMENT("2","1000","预约"),
    ORDER_SERVICE_TYPE_PICK_UP("3","2001","接机"),
    ORDER_SERVICE_TYPE_CHARTERED("4","3000","包车"),
    ORDER_SERVICE_TYPE_SEND("5","2002","送机");


    private String prState;
    private String bcState;
    private String stateName;

    OrderServiceType(String prState,String bcState,String stateName){
        this.prState = prState;
        this.bcState = bcState;
        this.stateName = stateName;
    }


    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPrState() {
        return prState;
    }

    public void setPrState(String prState) {
        this.prState = prState;
    }

    public String getBcState() {
        return bcState;
    }

    public void setBcState(String bcState) {
        this.bcState = bcState;
    }
}
