package com.hq.ecmp.constant;

import lombok.Getter;

/**
 * @author crk
 */
@Getter
public enum MsgUserConstant {

    MESSAGE_USER_USER("申请人",1,"user"),
    MESSAGE_USER_DRIVER("司机",2,"driver"),
    MESSAGE_USER_DISPATCHER("调度员",3,"dispatcher"),
    MESSAGE_USER_APPROVAL("审批员",4,"approval"),
    MESSAGE_USER_APPLICANT("乘车人",5,"applicant");

    private String name;
    private int type;
    private String desp;
    MsgUserConstant(String name, int type, String desp){
        this.name = name;
        this.type = type;
        this.desp = desp;
    }

    public static String applyUsers(){
        return MESSAGE_USER_USER.getType()+","+MESSAGE_USER_APPROVAL.getType()+","+MESSAGE_USER_APPLICANT.getType();
    }
}
