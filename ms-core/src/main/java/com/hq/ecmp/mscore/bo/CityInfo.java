package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 * 城市信息
 * 
 * @author cm
 *
 */

public class CityInfo {
	private Integer cityId;

	private String cityName;//城市名称

	private String cityCode;//城市编码

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	
}
