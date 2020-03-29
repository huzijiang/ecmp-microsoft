package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.mscore.service.ThirdService;
import com.hq.ecmp.mscore.vo.FlightInfoVo;
import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.MacTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
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
        String result = OkHttpUtil.postForm(apiUrl + "/service/locateByLongitudeAndLatitude", paramMap);
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
}
