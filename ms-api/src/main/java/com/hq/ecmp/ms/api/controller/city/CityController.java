package com.hq.ecmp.ms.api.controller.city;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.bo.CityHistoryAddress;
import com.hq.ecmp.mscore.dto.CityDto;
import com.hq.ecmp.mscore.service.ChinaCityService;
import com.hq.ecmp.mscore.service.CityHistoryAddressService;
import com.hq.ecmp.mscore.service.ThirdService;

import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.MacTools;
import org.etsi.uri.x01903.v13.impl.CertIDTypeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.bo.WeatherAndCity;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/city")
public class CityController {

	private  static final Logger logger = LoggerFactory.getLogger(CityController.class);

	@Autowired
	private ChinaCityService cityService;
	@Autowired
	private ThirdService thirdService;

	@Autowired
	TokenService tokenService;

	@Autowired
	private CityHistoryAddressService cityHistoryAddressService;


	@Value("${thirdService.enterpriseId}")
	private String enterpriseId;
	@Value("${thirdService.licenseContent}")
	private String licenseContent;
	@Value("${thirdService.apiUrl}")
	private String apiUrl;

	@ApiOperation(value = "getCityByName", notes = "据城市名称模糊搜索城市列表 ", httpMethod = "POST")
	@PostMapping("/getCityByName")	
	public ApiResponse<List<CityInfo>> getCityByName(@RequestBody(required = false) CityDto cityDto) {
		return ApiResponse.success(cityService.queryCityInfoListByCityName(cityDto == null ? null : cityDto.getCityName(),null));
	}

	@ApiOperation(value = "getCityByNameAndRegimeId", notes = "据制度id和城市名称模糊搜索城市列表 ", httpMethod = "POST")
	@PostMapping("/getCityByNameAndRegimeId")
	public ApiResponse<List<CityInfo>> getCityByNameAndRegimeId(@RequestParam("regimenId") Long regimenId, @RequestParam("cityName") String cityName) {
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


	/**
	 *
	 * @param list
	 * @return
	 */
	@ApiOperation(value = "保存当前城市的历史地址", notes = "保存当前城市的历史地址 ", httpMethod = "POST")
	@PostMapping("/addCityAddress")
	public ApiResponse<String> addCityAddress(@RequestBody List<CityHistoryAddress> list) {
		Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
		try{
			if(cityHistoryAddressService.addCityAddress(userId,list)>0){
				return ApiResponse.success("保存当前城市的历史地址成功");
			}
		}catch(Exception e){
			logger.error("保存当前城市的历史地址异常",e);
		}
		return ApiResponse.error("保存当前城市的历史地址失败");
	}


	/***
	 *add by liuzb
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取当前城市输入的历史地址", notes = "获取当前城市输入的历史地址 ", httpMethod = "POST")
	@PostMapping("/getCityAddress")
	public ApiResponse<List<CityHistoryAddress>> getCityAddress(String cityCode,String cityName,String shortAddress) {
		Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
		try{
			List<CityHistoryAddress> list = cityHistoryAddressService.getCityAddress(userId,cityCode,cityName,shortAddress);
			if(null!=list && list.size()>0){
				return ApiResponse.success(list);
			}
			/***调用云端接口*/
			Map<String,Object> map = new HashMap<>();
			map.put("cityCode",cityCode);
			map.put("enterpriseId",enterpriseId);map.put("licenseContent",licenseContent);map.put("mac", MacTools.getMacList().get(0));
			String postJson = OkHttpUtil.postForm(apiUrl+"/basic/hotScenicSpots", map);
			return GsonUtils.jsonToBean(postJson, new TypeToken<ApiResponse<List<CityHistoryAddress>>>() {}.getType());
		}catch(Exception e){
			logger.error("获取当前城市输入的历史地址异常",e);
		}
		return ApiResponse.error("获取当前城市输入的历史地址失败");
	}

}
