package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.bo.CityInfo;

public interface ChinaCityService {
	public String queryCityCodeByCityName(String cityName);
	
	
	public List<CityInfo> queryCityInfoListByCityName(String cityName);

	CityInfo queryCityByCityCode(String cityId);
}
