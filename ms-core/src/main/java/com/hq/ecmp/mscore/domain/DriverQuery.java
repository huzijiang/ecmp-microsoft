package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.dto.Page;

import lombok.Data;

@Data
public class DriverQuery extends Page {
		String name;//姓名or工号or手机号
		
		Long carGroupId;//车队编号
}
