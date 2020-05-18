package com.hq.ecmp.constant;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;


/**
 * @author crk
 */
@Getter
public enum MsgConstant {

    MESSAGE_T001("申请通知","M001","您有1条用车申请待审批，点击查看详情"),
    MESSAGE_T002("待审批通知","M002","您有1条用车审批待处理，点击查看详情"),
    MESSAGE_T003("调度通知","M003","您有1条调度任务待处理，点前前往"),
    MESSAGE_T004("改派通知","M004","您有1条改派任务待处理，点击前往"),
    MESSAGE_T005("订单取消","M005","您有1条任务被取消, 点击查看详情"),
    MESSAGE_T006("行程通知","M006","你有1条正在进行中的行程,点击查看详情"),
    MESSAGE_T007("新任务通知","M007","您有1条新的任务指派，点击查看详情"),
    MESSAGE_T008("任务即将开始通知","M008","您有1条任务还有30分钟即将开始服务，点击查看详情"),
    MESSAGE_T009("审批通过","M009","您有1条用车申请已通过，点击查看详情"),
    MESSAGE_T010("审批驳回","M010","您有1条用车申请已驳回，点击查看详情"),
    MESSAGE_T011("改派成功","M011","您有1条改派任务已通过，点击查看详情"),
    MESSAGE_T012("改派驳回","M012","您有1条改派任务已驳回，点击查看详情"),
    MESSAGE_T013("派车通知","M013","您有1条用车申请已派车，点击查看详情"),
    MESSAGE_T014("换车通知","M014","您有1条行程被更换车辆, 点击查看详情"),
    MESSAGE_T015("行程改派通知","M015","您有1条行程被改派，点击查看详情"),
    MESSAGE_T999("其他","M999","");


    private String desp;
    private String type;
    private String desc;
    MsgConstant(String desp, String type,String desc){
        this.desp = desp;
        this.type = type;
        this.desc = desc;
    }

    public static String getDespByType(String type) {
        for (MsgConstant c : MsgConstant.values()) {
            if (c.getType().equals(type)) {
                return c.getDesc();
            }
        }
        return "";
    }

    public static String applyAndApprove(){
        return MESSAGE_T001.getType()+","+MESSAGE_T002.getType();
    }

    public static String applyUserCategry(){
        return MESSAGE_T001.getType()+","+","+MESSAGE_T015.getType()
                +","+ MESSAGE_T006.getType()
                +","+ MESSAGE_T009.getType()+","+MESSAGE_T010.getType()
                +","+ MESSAGE_T013.getType()+","+MESSAGE_T015.getType()
                ;
    }

    public static String dispatchCategry(){
        return MESSAGE_T003.getType()+","+MESSAGE_T004.getType()+","+MESSAGE_T014.getType() ;
    }
    public static String driverCategry(){
        return MESSAGE_T005.getType()+","+MESSAGE_T007.getType()
                +","+MESSAGE_T011.getType()+","+MESSAGE_T012.getType()
                +","+MESSAGE_T014.getType() ;
    }
}
