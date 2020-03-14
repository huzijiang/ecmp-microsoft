package com.hq.ecmp.constant;
import lombok.Getter;


/**
 * @author crk
 */
@Getter
public enum MsgTypeConstant {

    MESSAGE_TYPE_T001("业务消息","T001"),
    MESSAGE_TYPE_T002("","TOO2"),
    MESSAGE_TYPE_T003("","T003"),
    MESSAGE_TYPE_T004("","T004");

    private String desp;
    private String type;
    MsgTypeConstant(String desp, String type){
        this.desp = desp;
        this.type = type;
    }
}
