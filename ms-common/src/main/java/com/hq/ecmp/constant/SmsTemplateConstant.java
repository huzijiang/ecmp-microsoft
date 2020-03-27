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





}
