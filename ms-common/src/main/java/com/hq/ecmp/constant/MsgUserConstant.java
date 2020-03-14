package com.hq.ecmp.constant;

import lombok.Getter;

/**
 * @author crk
 */
@Getter
public enum MsgUserConstant {

    MESSAGE_USER_USER("乘客",1,"user"),
    MESSAGE_USER_DRIVER("司机",2,"driver"),
    MESSAGE_USER_DISPATCHER("调度员",3,"dispatcher"),
    MESSAGE_USER_APPROVAL("审批员",4,"approval");

    private String name;
    private int type;
    private String desp;
    MsgUserConstant(String name, int type, String desp){
        this.name = name;
        this.type = type;
        this.desp = desp;
    }
}
