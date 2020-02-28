package com.hq.ecmp.ms.api.controller.city;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.bo.CityInfo;
import com.hq.ecmp.mscore.bo.WeatherAndCity;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/city")
public class CityController {
	
	@ApiOperation(value = "getCityByName", notes = "据城市名称模糊搜索城市列表 ", httpMethod = "POST")
	@PostMapping("/getCityByName")	
	public ApiResponse<List<CityInfo>> getCityByName(String cityName) {
		// 调用云端服务获取城市
		//TODO
		return null;
	}
	
	@ApiOperation(value = "getIndexInfo", notes = "获取首页信息 ", httpMethod = "POST")
	@PostMapping("/getIndexInfo")
	public ApiResponse<WeatherAndCity> getIndexInfo(Map<String, Object> map) {
		WeatherAndCity weatherAndCity = new WeatherAndCity();
		//获取今天是周几
		String week = DateUtils.getWeek();
		weatherAndCity.setWeek(week);
		//获取月 天
		weatherAndCity.setMonthAndDay(DateUtils.getMonthAndToday());
		// 调用云端服务获取城市   天气  
		//TODO
		weatherAndCity.setCityName("北京");
		weatherAndCity.setWeather("16 ℃ 晴");
		return ApiResponse.success(weatherAndCity);
	}
}
