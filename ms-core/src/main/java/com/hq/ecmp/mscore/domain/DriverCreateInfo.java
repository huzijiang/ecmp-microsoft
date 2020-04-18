package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DriverCreateInfo {

	    String driverName;//司机名字
		String mobile;//电话号码
		String gender;//性别   1111-男 0000-女

		Long carGroupId;//车队

		String idCard;//身份证号

		String licenseType;//驾照类型    C1  B2   B1  A2   A1

		String licenseNumber;//驾驶证号码

		String licensePhoto;//驾驶证照

		Date licenseInitIssueDate;//初次领证日期

		Date licenseIssueDate;//驾驶证开始时间

		Date licenseExpireDate;//驾驶证到期时间

		List<Long> carId;//可用车辆

		String jobNumber;//工号

		Long userId;

		Long optUserId;//操作人员

		String lockState;

		Long driverId;
		Date updateTime;
	    String state;
	    Date createTime;
	    Long createBy;

		
}
