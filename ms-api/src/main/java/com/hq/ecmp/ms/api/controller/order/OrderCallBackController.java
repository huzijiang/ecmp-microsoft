package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName OrderBackController
 * @Description TODO
 * @Author yj
 * @Date 2020/3/13 16:23
 * @Version 1.0
 */
@RestController
@RequestMapping("/orderCallBack")
@Api(value = "后台管理-订单模块")
public class OrderCallBackController {

    @Resource
    @Lazy
    private OrderInfoTwoService iOrderInfoService;

    @ApiOperation(value = "网约车订单状态回调接口")
    @RequestMapping(value = "/callBackOrderState")
    public ApiResponse callBackOrderState(@RequestParam("json") String json){
        if (StringUtils.isBlank(json)){
            return ApiResponse.success("参数为空");
        }
        try {
            iOrderInfoService.callBackOrderState(json);
            return ApiResponse.success("订单状态回调成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

}
