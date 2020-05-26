package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum CostConfigModeEnum {

	Config_mode_CA00("CA00","车+驾驶员"),
	Config_mode_CA01("CA01","仅用车"),
	Config_mode_CA10("CA10","仅用驾驶员"),
	Config_mode_CA11("CA11","车,司机都不用"),
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

	CostConfigModeEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		CostConfigModeEnum[] hintEnums = CostConfigModeEnum.values();
		for (CostConfigModeEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = CostConfigModeEnum.getParam();
		return param.get(key);
	}

}
