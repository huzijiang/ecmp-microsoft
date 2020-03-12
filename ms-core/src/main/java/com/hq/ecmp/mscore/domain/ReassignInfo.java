package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;

/**
 * 改派记录
 * @author cm
 *
 */
@Data
public class ReassignInfo {
	
		Long orderId;
		
		String reassignReason;//改派原因
		
		String driverName;//申请司机名字
		
		String driverPhone;//申请司机电话
		
		String approveName;//审批人名字
		
		String approvePhone;//审批人电话
		
		Date applyDate;//申请时间
		
		Date approveDate;//审批时间
}
