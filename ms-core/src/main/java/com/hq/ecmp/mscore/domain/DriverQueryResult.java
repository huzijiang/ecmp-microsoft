package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;

@Data
public class DriverQueryResult {

	Long driverId;

	String driverName;

	String mobile;

	Long userId;

	String company;// 所属公司

	String carGroupName;

	Integer ownCarCount;// 可用车辆

	String state;// 状态

	String itIsFullTime;// Z000 合同制 Z001 在编 Z002 外聘 Z003 借调
	
	String gender;//性别   1111-男 0000-女
	
	String idCard;//身份证号
	
	String licenseType;//驾照类型    C1  B2   B1  A2   A1
	
	String licenseNumber;//驾驶证号码
	
	String licensePhoto;//驾驶证照
	
	Date licenseInitIssueDate;//初次领证日期
	
	Date licenseExpireDate;//驾驶证到期时间
	  
}
