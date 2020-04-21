package com.hq.ecmp.constant;
import lombok.Getter;


/**
 * @author crk
 */
@Getter
public enum MsgConstant {

    MESSAGE_T001("申请通知","M001"),
    MESSAGE_T002("待审批通知","M002"),
    MESSAGE_T003("调度通知","M003"),
    MESSAGE_T004("改派通知","M004"),
    MESSAGE_T005("订单取消","M005"),
    MESSAGE_T006("行程通知","M006"),
    MESSAGE_T007("新任务通知","M007"),
    MESSAGE_T008("任务即将开始通知","M008"),
    MESSAGE_T009("审批通过","M009"),
    MESSAGE_T010("审批驳回","M010"),
    MESSAGE_T011("改派成功","M011"),
    MESSAGE_T012("改派驳回","M012"),
    MESSAGE_T013("派车通知","M013"),
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

    public static String applyAndApprove(){
        return MESSAGE_T001.getType()+","+MESSAGE_T002.getType();
    }
}
