package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderHistoryTraceDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName OrderBackController
 * @Description TODO
 * @Author yj
 * @Date 2020/3/13 16:23
 * @Version 1.0
 */
@RestController
@RequestMapping("/orderBack")
@Api(value = "后台管理-订单模块")
public class OrderBackController {

    @Resource
    @Lazy
    private IOrderInfoService iOrderInfoService;

    @ApiOperation(value = "订单列表查询")
    @PostMapping(value = "/getOrderList")
    public ApiResponse<List<OrderListBackDto>> getOrderList(@RequestBody  OrderListBackDto orderListBackDto){
        List<OrderListBackDto> orderListBackDtos;
        try {
            orderListBackDtos  = iOrderInfoService.getOrderListBackDto(orderListBackDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(orderListBackDtos);
    }

    @ApiOperation(value = "订单详情查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "orderNo", value = "orderNo", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping("/getOrderListDetail")
    public ApiResponse<OrderDetailBackDto> getOrderListDetail(@RequestParam("orderNo") String orderNo) {
        OrderDetailBackDto orderListDetail = null;
        try {
            orderListDetail = iOrderInfoService.getOrderListDetail(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(orderListDetail);
    }

    @ApiOperation("订单历史轨迹")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "orderNo",value = "orderNo",required = true,paramType = "query",dataType = "String")
    )
    @PostMapping("/getOrderHistoryTrace")
    public ApiResponse<List<OrderHistoryTraceDto>> getOrderHistoryTrace(@RequestParam("orderNo") String orderId){
        List<OrderHistoryTraceDto> orderHistoryTraceDtos = null;
        try {
            orderHistoryTraceDtos = iOrderInfoService.getOrderHistoryTrace(Long.parseLong(orderId));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取轨迹失败");
        }
        return ApiResponse.success(orderHistoryTraceDtos);
    }
}
