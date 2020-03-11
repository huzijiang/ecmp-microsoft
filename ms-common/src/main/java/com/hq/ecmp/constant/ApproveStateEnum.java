package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum ApproveStateEnum {

	APPROVE_PASS("pass","审批通过"),
	APPROVE_FAIL("fail","审批驳回"),
	NOT_ARRIVED_STATE("ST00","未到达"),
	WAIT_APPROVE_STATE("ST01","待审批"),
	COMPLETE_APPROVE_STATE("ST02","已审批"),
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

	ApproveStateEnum(String key, String desc) {
		this.key=key;
		this.desc=desc;
	}
	public static Map<String, String> getParam(){
		Map<String, String> map = Maps.newHashMap();
		ApproveStateEnum[] hintEnums = ApproveStateEnum.values();
		for (ApproveStateEnum hintEnum : hintEnums) {
			map.put(hintEnum.getKey(), hintEnum.getDesc());
		}
		return map;
	}
	public static String format(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		Map<String, String> param = ApproveStateEnum.getParam();
		return param.get(key);
	}
}
