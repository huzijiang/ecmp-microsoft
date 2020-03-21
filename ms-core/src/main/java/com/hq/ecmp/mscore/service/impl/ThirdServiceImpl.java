package com.hq.ecmp.mscore.service.impl;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.mscore.service.ThirdService;
import com.hq.ecmp.mscore.vo.FlightInfoVo;
import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.MacTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
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
            String postJson = OkHttpUtil.postForm(apiUrl+"/service/getFlightInfo", param);
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
}
