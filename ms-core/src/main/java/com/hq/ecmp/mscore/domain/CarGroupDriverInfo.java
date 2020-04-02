package com.hq.ecmp.mscore.domain;

import java.util.List;

import lombok.Data;

@Data
public class CarGroupDriverInfo {
		
	String companyName;//公司名称
	
	String deptName;//部门名称
	
	String deptLeader;//部门负责人
	
	Integer deptUserNum;//部门下的用户人数
	
	
	List<DriverQueryResult> driverList;//司机列表
}
