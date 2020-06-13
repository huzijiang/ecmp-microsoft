package com.hq.ecmp.ms.api.controller.third;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.ms.api.vo.third.HolidaysVO;
import com.hq.ecmp.ms.api.vo.third.LocationVO;
import com.hq.ecmp.mscore.dto.CarLevelAndPriceDTO;
import com.hq.ecmp.mscore.dto.FlightInfoDTO;
import com.hq.ecmp.mscore.dto.LocationDTO;
import com.hq.ecmp.mscore.vo.CarCostVO;
import com.hq.ecmp.mscore.vo.FlightInfoVo;
import com.hq.ecmp.util.ObjectUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @ApiOperation(value = "location", notes = "根据用户输入的短地址，调用第三方接口返回可用的地址列表及相应的坐标", httpMethod = "POST")
    @PostMapping(value = "/location")
    public ApiResponse<List<LocationVO>> location( @RequestBody LocationDTO locationDTO) {

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
                .postForm(apiUrl + "/service/enterpriseOrderGetCalculatePrice", map);
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

    @ApiOperation(value = "loadDepartment", notes = "调用第三方航班查询服务", httpMethod = "POST")
    @RequestMapping(value = "/loadDepartment", method = RequestMethod.POST)
    public ApiResponse<List<FlightInfoVo>> loadDepartment(@RequestBody FlightInfoDTO flightInfoDTO) {
        //查询航班、具体返回值需根据第三方接口定义
        try {
            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = ObjectUtils.objectToMap(flightInfoDTO);
            map.put("mac", macAddress);
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            String postJson = OkHttpUtil.postForm(apiUrl + "/service/getFlightInfo", map);
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

    @ApiOperation(value = "loadHolidays", notes = "加载法定节假日信息", httpMethod = "POST")
    @RequestMapping(value = "/loadHolidays", method = RequestMethod.POST)
    public ApiResponse<HolidaysVO> loadHolidays() {
        try {

            String macAddress = MacTools.getMacList().get(0);
            Map<String, Object> map = Maps.newHashMap();
            map.put("mac", macAddress);
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            //必须传个空的，不然报错
            map.put("year", "");
            String postJson = OkHttpUtil.postForm(apiUrl + "/basic/holidays", map);

            if (StringUtils.isEmpty(postJson)) {
                return ApiResponse.success("未查询到法定节假日信息");
            }
            JSONObject parseObject = JSONObject.parseObject(postJson);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端接口查询法定节假日信息失败!");
            }
            String data = parseObject.getString("data");
            HolidaysVO holidaysVO = JSONObject.parseObject(data, HolidaysVO.class);
            return ApiResponse.success(holidaysVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("加载法定节假日信息错误");
        }
    }

    /***
     * 获取云端电话
     */
    @ApiOperation(value = "获取云端电话", notes = "获取云端电话", httpMethod = "POST")
    @RequestMapping(value = "/getCloudsPhone", method = RequestMethod.POST)
    public ApiResponse<String> getCloudsPhone() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("enterpriseId", enterpriseId);
            map.put("licenseContent", licenseContent);
            map.put("mac", MacTools.getMacList().get(0));
            String postJson = OkHttpUtil.postForm(apiUrl + "/basic/400110", map);
            if (StringUtils.isEmpty(postJson)) {
                return ApiResponse.success("未查询到云端电话");
            }
            JSONObject parseObject = JSONObject.parseObject(postJson);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端接口查询法定节假日信息失败!");
            }
            String data = parseObject.getString("data");
            if (StringUtils.isEmpty(data)) {
                return ApiResponse.success("未查询到云端电话");
            }
            return ApiResponse.success("success", data);
        } catch (Exception e) {
            return ApiResponse.error("获取云端电话异常");
        }
    }

    @ApiOperation(value = "getCallTaxiCount", notes = "网约车自动约车约车次数获取接口", httpMethod = "POST")
    @RequestMapping(value = "/getCallTaxiCount", method = RequestMethod.POST)
    public ApiResponse getCallTaxiCount(@RequestParam("orderNo") String orderNo) {
        Integer count = 0;
        try {
            // MAC地址
            String macAdd = MacTools.getMacList().get(0);
            Map<String, Object> queryOrderStateMap = new HashMap<>();
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", macAdd);
            queryOrderStateMap.put("enterpriseOrderId", orderNo);
            String resultQuery = OkHttpUtil.postForm(apiUrl + "/service/orderCount",
                queryOrderStateMap);
            JSONObject parseObject = JSONObject.parseObject(resultQuery);
            if (!"0".equals(parseObject.getString("code"))) {
                return ApiResponse.error("调用云端获取约车次数失败!");
            }
            String data = parseObject.getString("data");
            if (StringUtils.isEmpty(data)) {
                return ApiResponse.success("未查询到云端电话");
            }
            return ApiResponse.success("success", data);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取次数失败");
        }
    }
}
