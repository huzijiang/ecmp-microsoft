package com.hq.ecmp.mscore.domain;

import lombok.Data;

@Data
public class DispatchDriverInfo {
	String driverName;//驾驶员名字
	
	String driverTel;//驾驶员电话号码
	
	String carType;//车型
	
	String carLicense;//车牌号
	
	String reassignmentApplyReason;//改派理由
	
	Long driveWaitTime;//等待时长  分钟
	
	
}
