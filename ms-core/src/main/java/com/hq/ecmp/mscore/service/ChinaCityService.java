package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.vo.CityInfo;

public interface ChinaCityService {
	public String queryCityCodeByCityName(String cityName);
	
	
	public List<CityInfo> queryCityInfoListByCityName(String cityName,Long regimeId);

	CityInfo queryCityByCityCode(String cityId);
}
