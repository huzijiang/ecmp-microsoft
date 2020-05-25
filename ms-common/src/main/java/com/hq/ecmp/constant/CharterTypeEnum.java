package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum CharterTypeEnum {

	NON_CHARTER("T000","非包车"),
	HALF_DAY_TYPE("T001","半日租(4小时)"),
	OVERALL_RENT_TYPE("T002","整日租(8小时)"),
	MORE_RENT_TYPE("T009","多日租");
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

	CharterTypeEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		CharterTypeEnum[] hintEnums = CharterTypeEnum.values();
		for (CharterTypeEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = CharterTypeEnum.getParam();
		return param.get(key);
	}
}
