package com.hq.ecmp.mscore.domain;

import java.util.Date;

/**
 * 派车信息
 * @author cm
 *
 */
public class SendCarInfo {
		String optUserName;//操作用户名字
		
		String optType;//1.提交派车申请   2.派车成功   3.提交改派申请   4。改派驳回
		
		Date optDate;//操作时间
}
