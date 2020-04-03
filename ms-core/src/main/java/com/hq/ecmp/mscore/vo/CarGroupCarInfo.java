package com.hq.ecmp.mscore.vo;

import java.util.List;

import lombok.Data;

@Data
public class CarGroupCarInfo {

	String companyName;// 公司名称

	String deptName;// 部门名称

	List<CarListVO> list;// 车辆信息
}
