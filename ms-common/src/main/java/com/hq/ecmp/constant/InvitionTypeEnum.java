package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum InvitionTypeEnum {

	USER("T001","员工"),
	DRIVER("T002","驾驶员"),
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

	InvitionTypeEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		InvitionTypeEnum[] hintEnums = InvitionTypeEnum.values();
		for (InvitionTypeEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = InvitionTypeEnum.getParam();
		return param.get(key);
	}
}
