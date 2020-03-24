package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum CarLeaveEnum {

	CAR_LEAVE_P001("P001","公务级"),
	CAR_LEAVE_P002("P002","行政级"),
	CAR_LEAVE_P003("P003","六座商务"),
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

	CarLeaveEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		CarLeaveEnum[] hintEnums = CarLeaveEnum.values();
		for (CarLeaveEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = CarLeaveEnum.getParam();
		return param.get(key);
	}

	public static String getAll(){
		return CAR_LEAVE_P001.getKey()+","+CAR_LEAVE_P002.getKey()+","+CAR_LEAVE_P003.getKey();
	}

}
