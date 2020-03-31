package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.service.ChinaProvinceService;
import com.hq.ecmp.mscore.vo.ProvinceCityVO;
import com.hq.ecmp.mscore.vo.ProvinceVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 省市
 * @author
 * @date 2020-3-11
 *
 */
 @RestController
 @RequestMapping("/province")
public class ProvinceCityController {

    @Autowired
    private ChinaProvinceService chinaProvinceService;
    /**
     * 省份查询
     * @param
     * @return list
     */
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
    @ApiOperation(value = "getProvinceCity",notes = "查询省份",httpMethod = "POST")
    @PostMapping("/getProvinceCity")
    public  ApiResponse<List<ProvinceCityVO>> getProvinceCity(String provinceCode){
        List<ProvinceCityVO>  cityList = chinaProvinceService.queryCityByProvince(provinceCode);
        return ApiResponse.success(cityList);
   }

}
