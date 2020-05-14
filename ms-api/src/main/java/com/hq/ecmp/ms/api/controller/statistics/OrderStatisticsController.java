package com.hq.ecmp.ms.api.controller.statistics;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.StatisticsOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/statistics/order")
public class OrderStatisticsController {

    @Autowired
    private StatisticsOrderService statisticsOrderService;

    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "orderEndOfCarModelOfServiceType",notes = "订单完单约车类型和服务类型统计",httpMethod = "POST")
    @PostMapping("/orderEndOfCarModelOfServiceType")
    public ApiResponse orderEndOfCarModelOfServiceType(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsOrderService.orderEndOfCarModelOfServiceType(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "orderEndOfTimeInterval",notes = "订单完单时段分布统计",httpMethod = "POST")
    @PostMapping("/orderEndOfTimeInterval")
    public ApiResponse orderEndOfTimeInterval(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsOrderService.orderEndOfTimeInterval(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "orderEndOfScene",notes = "订单场景分布统计",httpMethod = "POST")
    @PostMapping("/orderEndOfScene")
    public ApiResponse orderEndOfScene(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsOrderService.orderEndOfScene(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "orderEndOfCity",notes = "订单全国分布统计",httpMethod = "POST")
    @PostMapping("/orderEndOfCity")
    public ApiResponse orderEndOfCity(@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsOrderService.orderEndOfCity(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "orderEndOfDept",notes = "订单部门分布统计",httpMethod = "POST")
    @PostMapping("/orderEndOfDept")
    public ApiResponse orderEndOfDept(@RequestBody StatisticsParam statisticsParam){
        try {
            List<Long> longs = new ArrayList<>();
            longs.add(tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getDept().getParentId());
            statisticsParam.setDeptIds(longs);
            return statisticsOrderService.orderEndOfDept(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
}
