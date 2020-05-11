package com.hq.ecmp.ms.api.controller.city;

import java.util.List;
import java.util.Map;

import com.hq.ecmp.mscore.service.ChinaCityService;
import com.hq.ecmp.mscore.service.ThirdService;

import org.etsi.uri.x01903.v13.impl.CertIDTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.bo.WeatherAndCity;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/city")
public class CityController {

	@Autowired
	private ChinaCityService cityService;
	@Autowired
	private ThirdService thirdService; 
	
	@ApiOperation(value = "getCityByName", notes = "据城市名称模糊搜索城市列表 ", httpMethod = "POST")
	@PostMapping("/getCityByName")	
	public ApiResponse<List<CityInfo>> getCityByName(@RequestParam(value = "cityName",required = false)  String cityName) {
		return ApiResponse.success(cityService.queryCityInfoListByCityName(cityName,null));
	}

	@ApiOperation(value = "getCityByNameAndRegimeId", notes = "据制度id和城市名称模糊搜索城市列表 ", httpMethod = "POST")
	@PostMapping("/getCityByNameAndRegimeId")
	public ApiResponse<List<CityInfo>> getCityByNameAndRegimeId(@RequestParam("regimenId") Long regimenId,@RequestParam("cityName") String cityName) {
		return ApiResponse.success(cityService.queryCityInfoListByCityName(cityName,regimenId));
	}
	
	@ApiOperation(value = "getIndex", notes = "获取首页信息 ", httpMethod = "POST")
	@PostMapping("/getIndex")
	public ApiResponse<WeatherAndCity> getIndexInfo(String longitude,String latitude) {
		WeatherAndCity weatherAndCity = new WeatherAndCity();
		//获取今天是周几
		String week = DateUtils.getWeek();
		weatherAndCity.setWeek(week);
		//获取月 天
		weatherAndCity.setMonthAndDay(DateUtils.getMonthAndToday());
		// 调用云端服务获取城市   天气  
		try {
			WeatherAndCity queryWeatherAndCity = thirdService.queryWeatherAndCity(longitude, latitude);
			if(null !=queryWeatherAndCity){
				String cityName = queryWeatherAndCity.getCity();
				weatherAndCity.setCityName(cityName);
				weatherAndCity.setWeather(queryWeatherAndCity.getWeatherDescription());
				if(StringUtil.isNotEmpty(cityName)){
					//通过城市简称查询城市编码
					String cityCode = cityService.queryCityCodeByCityName(cityName);
					weatherAndCity.setCityCode(cityCode);
				}
			}
			return ApiResponse.success(weatherAndCity);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error(e.getMessage());
		}
	}
}
