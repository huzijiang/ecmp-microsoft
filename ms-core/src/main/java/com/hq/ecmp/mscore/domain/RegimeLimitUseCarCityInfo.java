package com.hq.ecmp.mscore.domain;

import java.util.List;

import lombok.Data;

@Data
public class RegimeLimitUseCarCityInfo {
	List<String> canUseCityList;//可以使用的城市
	
	List<String> notCanUseCityList;//不能使用的城市
}
