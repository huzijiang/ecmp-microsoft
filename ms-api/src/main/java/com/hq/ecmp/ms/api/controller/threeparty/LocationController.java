package com.hq.ecmp.ms.api.controller.threeparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hq.ecmp.mscore.bo.CityInfo;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.AirportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.ms.api.dto.threeparty.LocationDto;
import com.hq.ecmp.ms.api.vo.threeparty.LocationInfoVo;
import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.util.MacTools;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Value("${thirdService.enterpriseId}") // 企业编号
	private String enterpriseId;
	@Value("${thirdService.licenseContent}") // 企业证书信息
	private String licenseContent;
	@Value("${thirdService.apiUrl}") // 三方平台的接口前地址
	private String apiUrl;
	@Resource
	private IOrderInfoService orderInfoService;
	@Resource
	private IJourneyNodeInfoService journeyNodeInfoService;
	@Autowired
	private IDriverHeartbeatInfoService driverHeartbeatInfoService;
	@Autowired
	private ChinaCityService chinaCityService;
	@Autowired
	private ThirdService thirdService;


	@ApiOperation(value = "queryByShortAddress", notes = "地址反查查询 ", httpMethod = "POST")
	@PostMapping("/queryByShortAddress")
	public ApiResponse<List<LocationInfoVo>> queryByShortAddress(@RequestParam("cityName") String cityName, @RequestParam("shortAddress") String shortAddress) {
		String cityCode = chinaCityService.queryCityCodeByCityName(cityName);// 查询城市编码
		if (null == cityCode) {
			return ApiResponse.error("未查询到城市的编码!");
		}
		try {
			// MAC地址
			List<String> macList = MacTools.getMacList();
			String macAdd = macList.get(0);
			// 调用云端接口 通过短地址反查地址详情
			Map<String, Object> queryOrderStateMap = new HashMap<>();
			queryOrderStateMap.put("enterpriseId", enterpriseId);
			queryOrderStateMap.put("licenseContent", licenseContent);
			queryOrderStateMap.put("mac", macAdd);
			queryOrderStateMap.put("cityId", cityCode);
			queryOrderStateMap.put("shortAddress", shortAddress);
			queryOrderStateMap.put("cityName", cityName);
			String resultQuery = OkHttpUtil.postForm(apiUrl + "/service/locateSearchByShortName", queryOrderStateMap);
			 JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
             if(!"0".equals(jsonObjectQuery.getString("code"))){
               return ApiResponse.error("调用云端接口地址反查失败!");
             }
             Object object = jsonObjectQuery.get("data");
             if(null==object){
            	 return ApiResponse.success("调用云端接口地址反查未查询到数据");
             }
             List<LocationInfoVo> list = JSONObject.parseArray(object.toString(), LocationInfoVo.class);
             return ApiResponse.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("地址反查失败!");
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> macList = MacTools.getMacList();
		String macAdd = macList.get(0);
		System.out.println(macAdd);
	}

	@ApiOperation(value = "queryAddress", notes = "司机获取乘客/司机位置信息 ", httpMethod = "POST")
	@PostMapping("/queryAddress")
	public ApiResponse<LocationInfoVo> queryAddress(@RequestBody LocationDto locationDto) {
		try {
			OrderInfo orderInfo = orderInfoService.selectOrderInfoById(locationDto.getOrderId());
			String latitude = null;
			String longitude = null;
			if (locationDto.getUserRole() == 0) {// 司机
				DriverHeartbeatInfo driverHeartbeatInfo = driverHeartbeatInfoService
						.findNowLocation(orderInfo.getDriverId(), orderInfo.getOrderId());
				latitude = driverHeartbeatInfo.getLatitude().stripTrailingZeros().toPlainString();
				longitude = driverHeartbeatInfo.getLongitude().stripTrailingZeros().toPlainString();
				return ApiResponse.success(new LocationInfoVo(latitude, longitude));
			} else {
				JourneyNodeInfo nodeInfo = journeyNodeInfoService.selectJourneyNodeInfoById(orderInfo.getNodeId());
				latitude = nodeInfo.getPlanBeginLatitude();
				longitude = nodeInfo.getPlanBeginLongitude();
				return ApiResponse.success(new LocationInfoVo(nodeInfo.getPlanBeginLongAddress(),
						nodeInfo.getPlanBeginAddress(), latitude, longitude));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("获取审批详情异常");
		}
	}

	@ApiOperation(value = "findAirportList", notes = "根据城市code获取机场列表 ", httpMethod = "POST")
	@PostMapping("/findAirportList")
	public ApiResponse<List<AirportVo>> findAirportList(@RequestBody Map<String,Object> map) {
		try {
			String cityCode = (String)map.get("cityCode");
			CityInfo city =chinaCityService.queryCityByCityCode(cityCode);
			map.put("cityId",city.getCityId());
			map.put("enterpriseId", enterpriseId);
			map.put("licenseContent", licenseContent);
			map.put("mac", MacTools.getMacList().get(0));
			String postJson = OkHttpUtil.postForm(apiUrl + "/service/getAirPortInfo", map);
			JSONObject parseObject = JSONObject.parseObject(postJson);
			String data = parseObject.getString("data");
			if(!"0".equals(parseObject.getString("code"))){
				return ApiResponse.error("该城市未有机场!");
			}
			List<AirportVo> list = JSONObject.parseArray(data, AirportVo.class);
			return ApiResponse.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("获取机场列表异常");
		}
	}

	@ApiOperation(value = "getCustomerPhone", notes = "根据城市code获取机场列表 ", httpMethod = "POST")
	@PostMapping("/getCustomerPhone")
	public ApiResponse<String> getCustomerPhone() {
		try {
			String customerPhone = thirdService.getCustomerPhone();
			return ApiResponse.success("获取客服成功",customerPhone);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("获取客服超时");
		}
	}


}
