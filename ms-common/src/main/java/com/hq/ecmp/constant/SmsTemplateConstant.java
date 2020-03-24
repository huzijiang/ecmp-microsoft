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
    //审批通过后给申请人发短信模板
    public static final String approve_pass_applyUser = "approve_pass_applyUser";
    //公务审批驳回后给申请人短信模板
    public static final String approve_reject_Business = "approve_reject_Business";
    //差旅审批驳回给申请人短信模板
    public static final String approve_reject_Travel = "approve_reject_Travel";
}
