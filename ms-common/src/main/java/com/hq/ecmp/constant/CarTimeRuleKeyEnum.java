package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum CarTimeRuleKeyEnum {

	CAR_TIME_KAY_R201("R201","工作日","今日"),
	CAR_TIME_KAY_R202("R202","工作日","次日"),
	CAR_TIME_KAY_R301("R301","周末和法定节假日","今日"),
	CAR_TIME_KAY_R302("R302","周末和法定节假日","次日"),
	CAR_TIME_KAY_R101("R101","周一","今日"),
	CAR_TIME_KAY_D101("D101","周一","次日"),
	CAR_TIME_KAY_R102("R102","周二","今日"),
	CAR_TIME_KAY_D102("D102","周二","次日"),
	CAR_TIME_KAY_R103("R103","周三","今日"),
	CAR_TIME_KAY_D103("D103","周三","次日"),
	CAR_TIME_KAY_R104("R104","周四","今日"),
	CAR_TIME_KAY_D104("D104","周四","次日"),
	CAR_TIME_KAY_R105("R105","周五","今日"),
	CAR_TIME_KAY_D105("D105","周五","次日"),
	CAR_TIME_KAY_R106("R106","周六","今日"),
	CAR_TIME_KAY_D106("D106","周六","次日"),
	CAR_TIME_KAY_R107("R107","周日","今日"),
	CAR_TIME_KAY_D107("D107","周日","次日"),
	;

	private String key;
	private String desc;//描述
	private String type;//描述

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	CarTimeRuleKeyEnum(String key, String desc,String type) {
		this.key=key;
		this.desc=desc;
		this.type=type;
	}
	public static Map<String, CarTimeRuleKeyEnum> getParam(){
		Map<String, CarTimeRuleKeyEnum> map = Maps.newHashMap();
		CarTimeRuleKeyEnum[] hintEnums = CarTimeRuleKeyEnum.values();
		for (CarTimeRuleKeyEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum);
		}
		return map;
	}
	public static CarTimeRuleKeyEnum format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, CarTimeRuleKeyEnum> param = CarTimeRuleKeyEnum.getParam();
		return param.get(key);
	}
}
