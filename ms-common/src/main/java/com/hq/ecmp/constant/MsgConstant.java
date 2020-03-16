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
    MESSAGE_T006("行程通知","M006"),
    MESSAGE_T007("新任务通知","M007"),
    MESSAGE_T999("其他","M999");


    private String desp;
    private String type;
    MsgConstant(String desp, String type){
        this.desp = desp;
        this.type = type;
    }

    public static String getDespByType(String type) {
        for (MsgConstant c : MsgConstant.values()) {
            if (c.getType().equals(type)) {
                return c.getDesp();
            }
        }
        return "";
    }
}
