package com.hq.ecmp.constant;

/**
 * 	消息通知内容模板
 * @author chenmi
 *
 */
public class MessageTemplateConstant {
	//调度自有车完成后发送给司机消息
	public static final String DISPATCH_CAR_COMPLETE_DRIVER="你有新的任务! 订单号%s。预定时间%s。乘客:%s,联系电话:%s。";

	//调度自有车完成后发送给申请人消息
	public static final String DISPATCH_CAR_COMPLETE_APPLY="您已成功预约%s的用车服务。司机%s将准时接驾，联系电话:%s，车型：%s，车牌号：%s。请您合理安排时间准时上车。祝您用车愉快！";
}
