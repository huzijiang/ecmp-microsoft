package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.bo.WeatherAndCity;
import com.hq.ecmp.mscore.dto.DirectionDto;
import com.hq.ecmp.mscore.service.ThirdService;
import com.hq.ecmp.mscore.vo.CarCostVO;
import com.hq.ecmp.mscore.vo.EstimatePriceVo;
import com.hq.ecmp.mscore.vo.FlightInfoVo;
import com.hq.ecmp.mscore.vo.ThridCarTypeVo;
import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.ObjectUtils;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FlightServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/20 18:06
 * @Version 1.0
 */

@Service
@Slf4j
public class ThirdServiceImpl implements ThirdService {

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public FlightInfoVo loadDepartment(String flightCode, String planDate) {
        FlightInfoVo flightInfoVo =null;
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String,Object> param = Maps.newHashMap();
            param.put("flightCode",flightCode);
            param.put("flightDate",planDate);
            param.put("mac",macAddress);
            param.put("enterpriseId",enterpriseId);
            param.put("licenseContent",licenseContent);
            log.info("航班信息入参：{}", param);
            String postJson = OkHttpUtil.postForm(apiUrl+"/service/getFlightInfo", param);
            log.info("航班信息返回结果：{}", postJson);
            Type type = new TypeToken<ApiResponse<List<FlightInfoVo>>>() {
            }.getType();
            ApiResponse<List<FlightInfoVo>> result = GsonUtils.jsonToBean(postJson, type);

            if (ApiResponse.SUCCESS_CODE==result.getCode()) {
                if (!CollectionUtils.isEmpty(result.getData())){
                     flightInfoVo = result.getData().get(0);
                }
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightInfoVo;
    }

    @Override
    public Map<String,String> locationByLongitudeAndLatitude(String longitude, String latitude) throws Exception {
        String longAddr;
        String shortAddr;
        Map<String,String> address = new HashMap<>();
        List<String> macList = MacTools.getMacList();
        String macAdd = macList.get(0);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("enterpriseId", enterpriseId);
        paramMap.put("licenseContent", licenseContent);
        paramMap.put("mac", macAdd);
        paramMap.put("longitude", String.valueOf(longitude));
        paramMap.put("latitude",String.valueOf(latitude));
        log.info("经纬度查长短地址入参：{}", paramMap);
        String result = OkHttpUtil.postForm(apiUrl + "/service/locateByLongitudeAndLatitude", paramMap);
        log.info("经纬度查长短地址结果：{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ApiResponse.SUCCESS_CODE!=jsonObject.getInteger("code")) {
            log.error("调用云端经纬度获取长短地址接口失败");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        longAddr = data.getString("addressFullName");
        shortAddr = data.getString("formatted_address");
        address.put("longAddr",longAddr);
        address.put("shortAddr",shortAddr);
        return address;
    }

    /**
     * 车型查预估价
     * @param estimatePriceVo
     * @return
     */
    @Override
    public List<CarCostVO> enterpriseOrderGetCalculatePrice(EstimatePriceVo estimatePriceVo) throws Exception {
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = ObjectUtils.objectToMap(estimatePriceVo);
            map.put("mac", macAddress);
            map.put("address", "");
            map.put("cpu", "");
            map.put("license", "");
            map.put("machineIp", "");
            map.put("name", "");
            map.put("os", "");
            map.put("platId", "");
            map.put("socialCreditCode", "");
            map.put("telephone", "");
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            log.info("预估计接口入参：{}", map);
            String postJson = OkHttpUtil.postForm(apiUrl + "/service/enterpriseOrderGetCalculatePrice", map);
            log.info("预估计接口结果：{}", postJson);
            ApiResponse<List<CarCostVO>> result = GsonUtils.jsonToBean(postJson, new com.google.gson.reflect.TypeToken<ApiResponse<List<CarCostVO>>>() {
            }.getType());
            if (ApiResponse.SUCCESS_CODE == result.getCode()) {
                return result.getData();
            }
        }catch(SocketTimeoutException e){
            e.printStackTrace();
            throw new SocketTimeoutException("预估价格查询超时");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("预估价格查询失败");
        }
        return null;
    }

    /**
     * 经纬度查询时长和里程
     * @param startPoint (格式：精度,纬度)
     * @param endPoint (格式：精度,纬度)
     * @return
     */

    @Override
    public DirectionDto drivingRoute(String startPoint, String endPoint) {
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("mac", macAddress);
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            map.put("startPoint", startPoint);
            map.put("endPoint", endPoint);
            log.info("出发地和目的地预估时长和里程接口入参：{}", map);
            String postJson = OkHttpUtil.postForm(apiUrl + "/service/drivingRoute", map);
            log.info("出发地和目的地预估时长和里程接口结果：{}", postJson);
            ApiResponse<DirectionDto> result = GsonUtils.jsonToBean(postJson, new com.google.gson.reflect.TypeToken<ApiResponse<DirectionDto>>() {
            }.getType());
            if(ApiResponse.SUCCESS_CODE == result.getCode()){
                return result.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public WeatherAndCity queryWeatherAndCity(String longitude, String latitude) throws Exception {
		if(StringUtil.isEmpty(longitude) || StringUtil.isEmpty(latitude)){
			  throw new Exception("经纬度不能为空");
		}
		 try {
			String macAddress = MacTools.getMacList().get(0);
			Map<String,Object>  map=new HashMap<String,Object>();
			  map.put("mac", macAddress);
			  map.put("enterpriseId", enterpriseId);
			  map.put("licenseContent", licenseContent);
			  map.put("latitude", latitude);
			  map.put("longitude", longitude);
			  log.info("天气城市查询接口入参；{}",map);
			  String postJson = OkHttpUtil.postForm(apiUrl + "/basic/getWeatherInfoByLatitudeAndLongitude", map);
			  log.info("天气城市查询接口返回;{}",postJson);
			  ApiResponse<WeatherAndCity> result = GsonUtils.jsonToBean(postJson, new com.google.gson.reflect.TypeToken<ApiResponse<WeatherAndCity>>() {
			  }.getType());
			  if(ApiResponse.SUCCESS_CODE == result.getCode()){
				  return result.getData();
			  }
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("天气城市查询失败");
		}
		return null;
	}

    @Override
    public String getCustomerPhone() throws Exception{
        Object phone = redisUtil.get(CommonConstant.CUSTOMER_PHONE);
        if (phone!=null){
            return phone.toString();
        }
        String macAddress = MacTools.getMacList().get(0);
        Map<String, Object> param = Maps.newHashMap();
        param.put("mac", macAddress);
        param.put("enterpriseId", enterpriseId);
        param.put("licenseContent", licenseContent);
        log.info("获取客服电话参数：{}", param);
        String postJson = OkHttpUtil.postForm(apiUrl + "/basic/400", param);
        log.info("获取客服电话返回结果：{}", postJson);
        Type type = new TypeToken<ApiResponse<String>>() {
        }.getType();
        ApiResponse<String> result = GsonUtils.jsonToBean(postJson, type);
        if (ApiResponse.SUCCESS_CODE == result.getCode()) {
            if (StringUtils.isNotEmpty(result.getData())) {
                redisUtil.set(CommonConstant.CUSTOMER_PHONE, result.getData(), 60 * 60 * 48);
            }
            return result.getData();
        } else {
            throw new Exception("获取客服电话异常");
        }
    }

    @Override
    public JSONObject threeCancelServer(Long orderId,String cancelReason)throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("enterpriseId", enterpriseId);
        paramMap.put("enterpriseOrderId", String.valueOf(orderId));
        paramMap.put("licenseContent", licenseContent);
        paramMap.put("mac",  MacTools.getMacList().get(0));
        paramMap.put("reason", cancelReason);
        log.info("网约车订单{}取消参数{}",orderId,paramMap);
        String result = OkHttpUtil.postForm(apiUrl + "/service/cancelOrder", paramMap);
        log.info("网约车订单{}取消返回结果{}",orderId,result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ApiResponse.SUCCESS_CODE != jsonObject.getInteger("code")) {
            throw new Exception("调用三方取消订单服务-》取消失败");
        }
        return jsonObject.getJSONObject("data");
    }

    @Override
    public List<ThridCarTypeVo> getOnlienCarType()throws Exception{
        Object redisValue = redisUtil.get(CommonConstant.ONLINE_CAR_TYPR_REDIS);
        if (redisValue!=null){
            String json = redisValue.toString();
            return JSONObject.parseArray(json,ThridCarTypeVo.class);
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("enterpriseId", enterpriseId);
        paramMap.put("licenseContent", licenseContent);
        paramMap.put("mac", MacTools.getMacList().get(0));
        log.info("网约车车型{}参数{}", paramMap);
        String result = OkHttpUtil.postForm(apiUrl + "/basic/carTypes", paramMap);
        log.info("网约车车型{}返回结果{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ApiResponse.SUCCESS_CODE != jsonObject.getInteger("code")) {
            throw new Exception("调用三方网约车车型列表  失败");
        }
        if (StringUtils.isNotEmpty(jsonObject.getString("data"))) {
            redisUtil.set(CommonConstant.ONLINE_CAR_TYPR_REDIS, jsonObject.getString("data"), 60 * 60 * 48);
        }
        return JSONObject.parseArray(jsonObject.getString("data"),ThridCarTypeVo.class);
    }

}
