package com.hq.ecmp.constant;
import lombok.Getter;


/**
 * @author crk
 */
@Getter
public enum MsgStatusConstant {

    MESSAGE_STATUS_T001("以读消息","1111"),
    MESSAGE_STATUS_T002("未读消息","0000");


    private String desp;
    private String type;
    MsgStatusConstant(String desp, String type){
        this.desp = desp;
        this.type = type;
    }
}
