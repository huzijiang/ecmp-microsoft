package com.hq.ecmp.ms.api.controller.statistics;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.StatisticsAbilityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinmq
 */
@RestController
@RequestMapping("/statistics/ability")
public class AbilityStatisticsController {

    @Autowired
    private StatisticsAbilityService statisticsAbilityService;

    @ApiOperation(value = "getCarGroupTreeByDeptIds",notes = "根据所选公司联动查询车队树",httpMethod = "POST")
    @PostMapping("/getCarGroupTreeByDeptIds")
    public ApiResponse getCarGroupTreeByDeptIds(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.getCarGroupTreeByDeptIds(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    @ApiOperation(value = "driverSum",notes = "驾驶员统计总数",httpMethod = "POST")
    @PostMapping("/driverSum")
    public ApiResponse driverSum(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.driverSum(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "driverSumByDate",notes = "驾驶员统计时间轴",httpMethod = "POST")
    @PostMapping("/driverSumByDate")
    public ApiResponse driverSumByDate(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.driverSumByDate(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    @ApiOperation(value = "driverSumByDimension",notes = "驾驶员统计不同维度的平均在线和平均服务",httpMethod = "POST")
    @PostMapping("/driverSumByDimension")
    public ApiResponse driverSumByDimension(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.driverSumByDimension(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "carSum",notes = "驾驶员统计总数",httpMethod = "POST")
    @PostMapping("/carSum")
    public ApiResponse carSum(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.carSum(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    @ApiOperation(value = "carSumByDate",notes = "车辆统计时间轴",httpMethod = "POST")
    @PostMapping("/carSumByDate")
    public ApiResponse carSumByDate(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.carSumByDate(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "carSumByByDimension",notes = "车辆统计不同维度的平均在线和平均服务",httpMethod = "POST")
    @PostMapping("/carSumByByDimension")
    public ApiResponse carSumByByDimension(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.carSumByByDimension(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "dispatchSum",notes = "调度运营统计总数",httpMethod = "POST")
    @PostMapping("/dispatchSum")
    public ApiResponse dispatchSum(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.dispatchSum(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "dispatchSumByDate",notes = "调度运营统计总数时间轴",httpMethod = "POST")
    @PostMapping("/dispatchSumByDate")
    public ApiResponse dispatchSumByDate(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsAbilityService.dispatchSumByDate(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
}
