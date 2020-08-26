package com.hq.ecmp.constant;

/**
 * @ClassName SmsTemplateConstant
 * @Description TODO
 * @Author yj
 * @Date 2020/3/23 9:52
 * @Version 1.0
 */
public class SmsTemplateConstant {
    //取消订单给司机发送短信的模板码
    public static final String CANCEL_ORDER_DRIVER = "cancel_order_driver";
    //取消订单（申请人和乘车人不是同一个时），给申请人发短信
    public static final String CANCEL_ORDER_APPLICANT = "cancel_order_applicant";
    //审批通过后给调度员发短信模板
    public static final String approve_pass_dispatcher = "approve_pass_dispatcher";
    //审批通过后给申请人不走调度模板
    public static final String approve_pass_notDispatch = "approve_pass_notDispatch";
    //审批通过后给申请人走调度模板
    public static final String approve_pass_doDispatch = "approve_pass_doDispatch";
    //审批通过后给申请人发差旅短信模板
    public static final String approve_pass_applyUser = "approve_pass_applyUser";
    //公务审批驳回后给申请人短信模板
    public static final String approve_reject_Business = "approve_reject_Business";
    //差旅审批驳回给申请人短信模板
    public static final String approve_reject_Travel = "approve_reject_Travel";
    //公务申请，给审批人发送短信的模板码
    public static final String OFFICIAL_APPLY_APPROVER = "official_apply_approver";
    //差旅申请，给审批人发送短信的模板码
    public static final String TRAVEL_APPLY_APPROVER = "travel_apply_approver";
    //约车成功以后给申请人发短信模板码
    public static final String NETCAR_SUCC_APPLICANT = "netcar_succ_applicant";
    //约车成功以后给乘车人（企业员工）发短信模板码
    public static final String NETCAR_SUCC_RIDER_ENTER = "netcar_succ_rider_enter";
    //约车成功以后给乘车人（非企业员工）发短信模板码
    public static final String NETCAR_SUCC_RIDER_NO_ENTER = "netcar_succ_rider_no_enter";
    //约车成功以后给司机发短信模板码
    public static final String NETCAR_SUCC_DRIVER = "netcar_succ_driver";
    //约车时间到了没约到车，超时短信-申请人
    public static final String NETCAR_FAIL_APPLICANT = "netcar_fail_applicant";
    //约车时间到了没约到车，超时短信-乘车人
    public static final String NETCAR_FAIL_RIDER = "netcar_fail_rider";
    //自有车司机到达给发短信-申请人
    public static final String PRICAR_DRIVER_READY_APPLICANT = "pricar_driver_ready_applicant";

    //司机开始服务-申请人
    public static final String PRICAR_DRIVER_START_SERVICE = "pricar_driver_start_service";
    //司机结束服务-
    public static final String PRICAR_DRIVER_SERVICE_END = "pricar_driver_service_end";
    //司机反馈评价-
    public static final String QUESTIONNAIRE = "questionnaire";



    //自有车司机到达给发短信-乘车人（企业员）
    public static final String PRICAR_DRIVER_ARR_RIDER_ENTER = "pricar_driver_arr_rider_enter";
    //自有车司机到达给发短信-乘车人（非企业员）
    public static final String PRICAR_DRIVER_ARR_RIDER_NO_ENTER = "pricar_driver_arr_rider_no_enter";
    //司机开始服务发送短信-申请人（乘车人和申请人不是一个人）
    public static final String DRIVER_BEGINSERVICE_APPLICANT = "driver_beginservice_applicant";
    //司机结束服务发送短信-申请人（乘车人和申请人不是一个人）
    public static final String DRIVER_COMPLETESERVICE_APPLICANT = "driver_completeservice_applicant";
    //取消订单-收费-发申请人短信模板
    public static final String CANCEL_ORDER_HAVEFEE_APPLICANT = "cancel_order_havefee_applicant";
    //取消订单-收费-发乘车人（企业员）短信模板
    public static final String CANCEL_ORDER_HAVEFEE_RIDER_ENTER = "cancel_order_havefee_rider_enter";
    //网约车司机已到达乘车人企业员工
    public static final String TAXI_DRIVER_ARR_RIDER_ENTER = "taxi_driver_arr_rider_enter";
    //网约车司机已到达乘车人非企业员工
    public static final String TAXI_DRIVER_ARR_RIDER_NO_ENTER = "taxi_driver_arr_rider_no_enter";
    //网约车司机已到达申请人
    public static final String TAXI_DRIVER_ARR_APPLY = "taxi_driver_arr_apply";
    //服务结束未确认行程申请人
    public static final String DRIVER_COMPLETE_NOT_CONFIRM = "driver_complete_not_confirm_apply";
    //服务结束未确认行程乘车人
    public static final String DRIVER_COMPLETE_NOT_CONFIRM_RIDER = "driver_complete_not_confirm_rider";

    public static final String REPLACE_CAR_DISPATCH_NOTICE = "replace_car_dispatch_notice";
    public static final String REPLACE_CAR_APPLY_NOTICE = "replace_car_apply_notice";
    public static final String REPLACE_CAR_RIDER_ENTERPRICE_NOTICE = "replace_car_rider_enterprice_notice";
    public static final String REPLACE_CAR_RIDER_NO_ENTERPRICE_NOTICE = "replace_car_rider_no_enterprice_notice";


    /**佛山短信模板集合--------------------**/
    /**调度驳回短信集合模块---start*/
    public static final String INNER_DISPATCH_REJECT_SALESMAN = "inner_dispatch_reject_salesman";
    public static final String INNER_DISPATCH_REJECT_USECARPEOPLE = "inner_dispatch_reject_useCarPeople";
    public static final String OUT_DISPATCH_REJECT_USECARPEOPLE = "out_dispatch_reject_useCarPeople";
    /**
     * 车辆+驾驶员调度，内部派车完成给外部调度员发短信模板,给用车人发短信模板，给申请人发短信模板
     */
    public static final String SMS_FOSHAN_SEND_CAR_TO_OUT_DISPATCHER = "sms_foshan_send_car_to_out_dispatcher";
    public static final String SMS_FOSAN_SEND_CAR_TO_USER_CAR = "sms_fosan_send_car_to_user_car";
    public static final String SMS_FOSAN_SEND_CAR_TO_APPLY_CAR = "sms_fosan_send_car_to_apply_car";

    /**
     * 派车成功给司机发短信模板
     */
    public static final String SMS_FOSAN_SEND_CAR_TO_DRIVER = "sms_fosan_send_car_to_driver";
    /**
     * p派车成功自驾给用车人发短信 申请人发短信
     */
    public static final String SMS_FOSAN_SEND_CAR_NO_SELF_TO_USER_CAR = "sms_fosan_send_car_no_self_to_user_car";
    public static final String SMS_FOSAN_SEND_CAR_NO_SELF_TO_APPLY_CAR = "sms_fosan_send_car_no_self_to_apply_car";







    /**用车申请短信集合模块---start*/
    public static final String SMS_FOSHAN_VEHICLE_APPLICANT = "sms_foshan_vehicle_applicant";
    public static final String SMS_FOSHAN_INTERNAL_DISPATCHER = "sms_foshan_Internal_dispatcher";
    public static final String SMS_FOSHAN_VEHICLE_APPLICANT_NOT  = "sms_foshan_vehicle_applicant_not";
    public static final String SMS_FOSHAN_INTERNAL_DISPATCHER_NOT = "sms_foshan_Internal_dispatcher_not";
    public static final String SMS_FOSHAN_EXTERNAL_VEHICLE_USER_YES = "sms_foshan_external_vehicle_user_yes";
    public static final String SMS_FOSHAN_EXTERNAL_DISPATCHER_YES = "sms_foshan_external_dispatcher_yes";
    public static final String SMS_FOSHAN_REVOKE_DRIVER = "sms_foshan_revoke_driver";
    public static final String SMS_FOSHAN_REVOKE_VEHICLE_USER = "sms_foshan_revoke_vehicle_user";
    public static final String SMS_FOSHAN_REVOKE_DISPATCHER = "sms_foshan_revoke_dispatcher";

    //外部调度员驳回短信模版
    public static final String OUT_DISPATCH_DISMISS_MESSAGE="out_dispatch_dismiss_message";


    //更改订单用车时间
    public static final String UPDATE_ORDER_USE_TIME_FOR_USER="update_order_use_time_for_user";
    public static final String UPDATE_ORDER_USE_TIME_FOR_APPLYER="update_order_use_time_for_applyer";
    public static final String UPDATE_ORDER_USE_TIME_FOR_DISPATCHER="update_order_use_time_for_dispatcher";
    public static final String UPDATE_ORDER_USE_TIME_FOR_DRIVER="update_order_use_time_for_driver";

    //内部调度员改派给外部调度员的时候，给申请人发短信
    public static final String SMS_NOTIFY_APPLY_PEO_MSG = "notify_apply_peo_msg";

    //内部调度员多次改派外部车队，给之前的外部车队发短信
    public static final String NOTIFY_OUT_GROUP_MSG = "notify_out_group_msg";

}
