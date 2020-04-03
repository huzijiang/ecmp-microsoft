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
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
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
@RequestMapping("/orderCallBack")
@Api(value = "后台管理-订单模块")
public class OrderCallBackController {

    @Resource
    private IOrderInfoService iOrderInfoService;

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
