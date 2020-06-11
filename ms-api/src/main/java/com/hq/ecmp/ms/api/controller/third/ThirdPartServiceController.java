package com.hq.ecmp.ms.api.controller.third;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.mscore.dto.CarLevelAndPriceDTO;
import com.hq.ecmp.mscore.dto.FlightInfoDTO;
import com.hq.ecmp.mscore.dto.LocationDTO;
import com.hq.ecmp.mscore.vo.CarCostVO;
import com.hq.ecmp.mscore.vo.FlightInfoVo;
import com.hq.ecmp.mscore.vo.LocationVO;
import com.hq.ecmp.util.ObjectUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 第三方服务
 *

 */
@RestController
@RequestMapping("/third")
public class ThirdPartServiceController {

    @Value("${thirdService.enterpriseId}") // 企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") // 企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}") // 三方平台的接口前地址
    private String apiUrl;
    @Value("${cloud.service.url}")
    private String httpurl;

    @ApiOperation(value = "location", notes = "根据用户输入的短地址，调用第三方接口返回可用的地址列表及相应的坐标", httpMethod = "POST")
    @PostMapping(value = "/location")
    public ApiResponse<List<LocationVO>> location(@RequestBody LocationDTO locationDTO) {

        try {
            // MAC地址
            String macAdd = MacTools.getMacList().get(0);
            // 调用云端接口 通过短地址反查地址详情
            Map<String, Object> queryOrderStateMap = ObjectUtils.objectToMap(locationDTO);
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", macAdd);

            String resultQuery = OkHttpUtil.postForm(apiUrl + "/service/locateSearchByShortName",
                queryOrderStateMap);
            JSONObject parseObject = JSONObject.parseObject(resultQuery);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端接口地址反查失败!");
            }
            String data = parseObject.getString("data");
            List<LocationVO> list = JSONObject.parseArray(data, LocationVO.class);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success("搜索地址信息异常!");
        }

    }

    @ApiOperation(value = "applyCalculateOrderPrice", notes = "预估价格接口", httpMethod = "POST")
    @RequestMapping(value = "/applyCalculateOrderPrice", method = RequestMethod.POST)
    public ApiResponse<List<CarCostVO>> estimate(@RequestBody CarLevelAndPriceDTO carLevelAndPriceDTO) {
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = ObjectUtils.objectToMap(carLevelAndPriceDTO);
            map.put("mac", macAddress);
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            String postJson = OkHttpUtil
                .postForm(httpurl + "/service/enterpriseOrderGetCalculatePrice", map);
            JSONObject parseObject = JSONObject.parseObject(postJson);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端接口获取预付价格失败!");
            }
            String data = parseObject.getString("data");
            List<CarCostVO> list = JSONObject.parseArray(data, CarCostVO.class);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success("获取预付价格异常!");
        }
    }

    @ApiOperation(value = "loadDepartment", notes = "调用第三方航班查询服务",httpMethod = "POST")
    @RequestMapping(value = "/loadDepartment", method = RequestMethod.POST)
    public ApiResponse<List<FlightInfoVo>> loadDepartment(@RequestBody FlightInfoDTO flightInfoDTO) {
        //查询航班、具体返回值需根据第三方接口定义
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = ObjectUtils.objectToMap(flightInfoDTO);
            map.put("mac", macAddress);
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            String postJson = OkHttpUtil.postForm(httpurl + "/service/getFlightInfo", map);
            if (StringUtils.isEmpty(postJson)) {
                return ApiResponse.success("航班信息未更新");
            }
            JSONObject parseObject = JSONObject.parseObject(postJson);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端接口查询航班服务失败!");
            }
            String data = parseObject.getString("data");
            List<FlightInfoVo> list = JSONObject.parseArray(data, FlightInfoVo.class);
            return ApiResponse.success(list);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("航班网络异常");
        }
    }
}
