package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.service.IOrderInfoService;
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
@RestController("/orderBack")
public class OrderBackController {

    @Resource
    private IOrderInfoService iOrderInfoService;

    @ApiOperation(value = "订单列表查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "header", dataType = "String")
    })
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

    @PostMapping("/getOrderListDetail")
    public ApiResponse getOrderListDetail(@RequestParam("orderNo") String orderNo){

        return null;
    }
}
