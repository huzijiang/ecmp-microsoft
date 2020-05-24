package com.hq.ecmp.constant;

import java.util.Arrays;
import java.util.List;

public enum OrderServiceType {

    //订单下单类型
    ORDER_SERVICE_TYPE_NOW("1","1000","即时用车，目前和预约一样"),
    ORDER_SERVICE_TYPE_APPOINTMENT("2","2000","预约"),
    ORDER_SERVICE_TYPE_PICK_UP("3","3000","接机"),
    ORDER_SERVICE_TYPE_CHARTERED("4","5000","包车"),
    ORDER_SERVICE_TYPE_SEND("5","4000","送机"),
    ORDER_SERVICE_TYPE_MORE_DAY("6","6000","多日租");


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

    public static String format(String key){
        OrderServiceType[] values = OrderServiceType.values();
        for (OrderServiceType orderServiceType:values){
            if (orderServiceType.bcState.equals(key)){
                return orderServiceType.stateName;
            }
        }
        return null;
    }

    public static List<String> getNetServiceType(){
        return Arrays.asList(ORDER_SERVICE_TYPE_NOW.getBcState(),ORDER_SERVICE_TYPE_APPOINTMENT.getBcState(),ORDER_SERVICE_TYPE_PICK_UP.getBcState(),ORDER_SERVICE_TYPE_SEND.getBcState());
    }
    
    public static List<String> getSendAndPick(){
        return Arrays.asList(ORDER_SERVICE_TYPE_PICK_UP.getBcState(),ORDER_SERVICE_TYPE_SEND.getBcState());
    }
}
