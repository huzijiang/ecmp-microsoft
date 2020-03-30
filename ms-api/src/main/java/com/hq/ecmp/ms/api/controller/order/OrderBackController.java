package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderHistoryTraceDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    private IOrderInfoService iOrderInfoService;

    @ApiOperation(value = "订单列表查询")
    @PostMapping(value = "/getOrderList")
    public ApiResponse<PageResult<OrderListBackDto>> getOrderList(@RequestBody  OrderListBackDto orderListBackDto){
        try {
            //根据与前台协商   首次进去订单管理 默认 10  - 1
            if(orderListBackDto.getPageSize()==null && orderListBackDto.getPageNum()==null){
                orderListBackDto.setPageSize(10);
                orderListBackDto.setPageNum(1);
            }
            //获取订单列表
            PageResult<OrderListBackDto> orderListBackDtos  = iOrderInfoService.getOrderListBackDto(orderListBackDto);
            return ApiResponse.success(orderListBackDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
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
