package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum InvitionStateEnum {

	APPROVING("S000","审核中"),
	APPROVEPASS("S001","审核通过"),
	APPROVEREJECT("S002","审核拒绝"),
	;

	private String key;
	private String desc;//描述

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	InvitionStateEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		InvitionStateEnum[] hintEnums = InvitionStateEnum.values();
		for (InvitionStateEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = InvitionStateEnum.getParam();
		return param.get(key);
	}
}
