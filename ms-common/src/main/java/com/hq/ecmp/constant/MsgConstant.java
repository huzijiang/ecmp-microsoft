package com.hq.ecmp.constant;
import lombok.Getter;


/**
 * @author crk
 */
@Getter
public enum MsgConstant {

    MESSAGE_T001("申请通知","M001"),
    MESSAGE_T002("审批通知","M002"),
    MESSAGE_T003("调度通知","M003"),
    MESSAGE_T004("订单改派","M004"),
    MESSAGE_T005("订单取消","M005"),
    MESSAGE_T999("其他","M999");


    private String desp;
    private String type;
    MsgConstant(String desp, String type){
        this.desp = desp;
        this.type = type;
    }
}
