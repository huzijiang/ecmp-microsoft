package com.hq.ecmp.ms.api.controller.statistics;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.service.StatisticsForAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ghb
 * @description 数据统计---管理员
 * @date 2020/6/8
 */


@RestController
@RequestMapping("/statistics/admin")
@Api(value = "数据统计-管理员")
public class StatisticsForAdminController {

    private static final Logger log = LoggerFactory.getLogger(StatisticsForAdminController.class);

    @Autowired
    private StatisticsForAdminService statisticsForAdminService;

    @ApiOperation(value = "driverOutranking",notes = "出车次数，用车费用排行",httpMethod = "POST")
    @PostMapping("/driverOutranking")
    public ApiResponse driverOutranking(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("出车次数，用车费用排行，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.driverOutranking(statisticsParam);
    }

    @ApiOperation(value = "vehicleExpenses",notes = "出车次数，用车费用排行",httpMethod = "POST")
    @PostMapping("/vehicleExpenses")
    public ApiResponse vehicleExpenses(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("出车次数，用车费用排行，前端传过来的参数为："+statisticsParam);
        return statisticsForAdminService.vehicleExpenses(statisticsParam);
    }

    @ApiOperation(value = "unitVehicle",notes = "单位用车统计",httpMethod = "POST")
    @PostMapping("/unitVehicle")
    public ApiResponse unitVehicle(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("单位用车统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.unitVehicle(statisticsParam);
    }

    @ApiOperation(value = "useOfMechanismVehicles",notes = "机关车辆使用统计",httpMethod = "POST")
    @PostMapping("/useOfMechanismVehicles")
    public ApiResponse useOfMechanismVehicles(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("机关车辆使用统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.useOfMechanismVehicles(statisticsParam);
    }

    @ApiOperation(value = "driverOut",notes = "驾驶员出车统计",httpMethod = "POST")
    @PostMapping("/driverOut")
    public ApiResponse driverOut(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("驾驶员出车统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.driverOut(statisticsParam);
    }

    @ApiOperation(value = "appointmentTime",notes = "预约时段统计",httpMethod = "POST")
    @PostMapping("/appointmentTime")
    public ApiResponse appointmentTime(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("预约时段统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.appointmentTime(statisticsParam);
    }

    @ApiOperation(value = "",notes = "车型使用统计",httpMethod = "POST")
    @PostMapping("/modelUse")
    public ApiResponse modelUse(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("车型使用统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.modelUse(statisticsParam);
    }

    @ApiOperation(value = "Leasing",notes = "租赁情况统计",httpMethod = "POST")
    @PostMapping("/leasing")
    public ApiResponse Leasing(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("租赁情况统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.leasing(statisticsParam);
    }

    @ApiOperation(value = "details",notes = "详情统计",httpMethod = "POST")
    @PostMapping("/details")
    public ApiResponse details(@RequestHeader("Authorization") String token, @RequestBody StatisticsForAdmin statisticsParam){
        log.info("详情统计，前端传过来的参数为："+statisticsParam);
            return statisticsForAdminService.details(statisticsParam);
    }
}
