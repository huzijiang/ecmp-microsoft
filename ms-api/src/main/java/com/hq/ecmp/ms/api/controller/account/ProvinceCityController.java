package com.hq.ecmp.ms.api.controller.account;


import com.google.gson.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.controller.city.CityController;
import com.hq.ecmp.mscore.bo.CityHistoryAddress;
import com.hq.ecmp.mscore.service.ChinaProvinceService;
import com.hq.ecmp.mscore.service.CityHistoryAddressService;
import com.hq.ecmp.mscore.vo.ProvinceCityVO;
import com.hq.ecmp.mscore.vo.ProvinceVO;
import com.hq.ecmp.util.GsonUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市
 * @author
 * @date 2020-3-11
 *
 */
 @RestController
 @RequestMapping("/province")
public class ProvinceCityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);


    @Autowired
    private ChinaProvinceService chinaProvinceService;

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
    /**
     * 省份查询
     * @param
     * @return list
     */
    @Log(title = "财务模块:查询省份", businessType = BusinessType.OTHER)
    @ApiOperation(value = "queryProvince",notes = "查询省份",httpMethod = "POST")
    @PostMapping("/queryProvince")
    public ApiResponse<List<ProvinceVO>> queryProvince(){
        List<ProvinceVO> provinceInfoList = chinaProvinceService.queryProvince();
        return ApiResponse.success(provinceInfoList);
    }

    /**
     * 省份下的城市
     * @param
     * @return list
     */
    @Log(title = "财务模块:查询省份下的城市", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getProvinceCity",notes = "查询省份下的城市",httpMethod = "POST")
    @PostMapping("/getProvinceCity")
    public  ApiResponse<List<ProvinceCityVO>> getProvinceCity(String provinceCode){
        List<ProvinceCityVO>  cityList = chinaProvinceService.queryCityByProvince(provinceCode);
        return ApiResponse.success(cityList);
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
            List<CityHistoryAddress> list = cityHistoryAddressService.getCityAddress(userId, cityCode, cityName, shortAddress);
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
