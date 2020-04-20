package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DriverQueryResult {

	Long driverId;

	String driverName;
	
	String jobNumber;

	String mobile;

	Long userId;

	String company;// 所属公司
	Long companyId;// 所属公司Id

	String carGroupName;
	
	Long carGroupId;//车队ID

	Integer ownCarCount;// 可用车辆

	String state;// 状态

	String itIsFullTime;// Z000 合同制 Z001 在编 Z002 外聘 Z003 借调
	
	String gender;//性别   1111-男 0000-女
	
	String idCard;//身份证号
	
	String licenseType;//驾照类型    C1  B2   B1  A2   A1
	
	String licenseNumber;//驾驶证号码
	
	String licensePhoto;//驾驶证照
	
	Date licenseInitIssueDate;//初次领证日期
	
	Date licenseIssueDate;//证照有效期 开始时间
	
	Date licenseExpireDate;//驾驶证到期时间
	
	List<Long> carId;//可使用车辆
	  
}
