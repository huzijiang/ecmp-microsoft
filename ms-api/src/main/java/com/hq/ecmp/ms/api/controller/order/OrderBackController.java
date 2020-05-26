package com.hq.ecmp.ms.api.controller.order;

import com.google.common.reflect.TypeToken;
import com.hq.api.system.domain.SysRole;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfoVo;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderHistoryTraceDto;
import com.hq.ecmp.mscore.dto.OrderInfoDTO;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Logger logger = LoggerFactory.getLogger(OrderBackController.class);

    @Autowired
    @Lazy
    private IOrderInfoService iOrderInfoService;
    @Resource
    private IEcmpUserFeedbackInfoService iEcmpUserFeedbackInfoService;
    @Autowired
    TokenService tokenService;



    @ApiOperation(value = "订单列表查询")
    @PostMapping(value = "/getOrderList")
    @Log(title = "订单管理", content = "订单列表", businessType = BusinessType.OTHER)
    public ApiResponse<PageResult<OrderListBackDto>> getOrderList(@RequestBody  OrderListBackDto orderListBackDto){
        try {
            //根据与前台协商   首次进去订单管理 默认 10  - 1
            if(orderListBackDto.getPageSize()==null && orderListBackDto.getPageNum()==null){
                orderListBackDto.setPageSize(10);
                orderListBackDto.setPageNum(0);
            }
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            orderListBackDto.setCompanyId(loginUser.getUser().getOwnerCompany());
            //获取订单列表
            PageResult<OrderListBackDto> orderListBackDtos  = iOrderInfoService.getOrderListBackDto(orderListBackDto,loginUser);
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
    @Log(title = "订单管理", content = "订单详情",businessType = BusinessType.OTHER)
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
    @Log(title = "订单管理",content = "订单历史轨迹", businessType = BusinessType.OTHER)
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

    @ApiOperation(value = "订单异议列表查询")
    @PostMapping(value = "/getObjectionOrderList")
    @Log(title = "订单管理", content = "订单异议列表", businessType = BusinessType.OTHER)
    public ApiResponse<PageResult<EcmpUserFeedbackInfoVo>> getObjectionOrderList(@RequestBody EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo){
        try {
            //获取订单列表
            //根据与前台协商   首次进去订单管理 默认 10  - 1
            if(ecmpUserFeedbackInfo.getPageSize()==null && ecmpUserFeedbackInfo.getPageNum()==null){
                ecmpUserFeedbackInfo.setPageSize(10);
                ecmpUserFeedbackInfo.setPageNum(0);
            }
            PageResult<EcmpUserFeedbackInfoVo> result  = iEcmpUserFeedbackInfoService.getObjectionOrderList(ecmpUserFeedbackInfo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
    }

    @ApiOperation(value = "回复订单异议")
    @PostMapping(value = "/replyObjectionOrder")
    @Log(title = "订单管理", content = "回复订单异议", businessType = BusinessType.OTHER)
    public ApiResponse replyObjectionOrder(@RequestBody EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo){
        try {
            //获取登陆用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
           int  i =  iEcmpUserFeedbackInfoService.replyObjectionOrder(ecmpUserFeedbackInfo,userId);
           if(i>0){
               return ApiResponse.success();
           }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success();
    }


    @ApiOperation(value = "订单管理补单提交功能")
    @PostMapping(value = "/supplementSubmit")
    @Log(title = "订单管理", content = "补单功能", businessType = BusinessType.INSERT)
    public ApiResponse supplementSubmit(@RequestBody OrderInfoDTO orderInfoDTO){
        try {
            //获取登陆用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            orderInfoDTO.setCompanyId(loginUser.getUser().getDept().getCompanyId());
            ApiResponse  apiResponse=  iEcmpUserFeedbackInfoService.supplementSubmit(orderInfoDTO);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
    }

    /**
     * 订单管理改单功能
     * add by liuzb
     */
    @ApiOperation(value = "订单管理改单功能")
    @PostMapping(value = "/updateTheOrder")
    public ApiResponse updateTheOrder(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo){
        try {
            if(iOrderInfoService.updateTheOrder(tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId(),orderServiceCostDetailRecordInfo)>0){
                return ApiResponse.success("订单管理改单成功");
            }
        } catch (Exception e) {
            logger.error("updateTheOrder error",e);
        }
        return ApiResponse.error("订单管理改单失败");
    }

    /***
     * 订单管理改单功能
     * add by liuzb
     * @param orderId
     * @return
     */
    @ApiOperation(value = "订单管理确认功能", notes = "订单管理确认功能 ", httpMethod = "POST")
    @PostMapping(value = "/orderConfirm")
    public ApiResponse orderConfirm(Long orderId){
        try {
            if(iOrderInfoService.orderConfirm(tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId(),orderId)>0){
                return ApiResponse.success("订单确认成功");
            }
        } catch (Exception e) {
            logger.error("orderConfirm error",e);
        }
        return ApiResponse.error("订单确认失败");
    }

    /***
     * 订单管理获取内部调度员电话
     * add by liuzb
     * @param orderId
     * @return
     */
    @ApiOperation(value = "获取内部调度员电话", notes = "获取内部调度员电话 ", httpMethod = "POST")
    @PostMapping(value = "/dispatcherPhone")
    public ApiResponse dispatcherPhone(Long orderId){
        try {
            return ApiResponse.success(iOrderInfoService.dispatcherPhone(orderId));
        } catch (Exception e) {
            logger.error("selectOrderList error",e);
        }
        return ApiResponse.error("获取内部调度员电话失败");
    }

    /***
     * 订单管理订单改派
     * add by liuzb
     * @param orderId
     * @return
     */
    @ApiOperation(value = "订单管理订单改派", notes = "订单管理订单改派 ", httpMethod = "POST")
    @PostMapping(value = "/orderReassignment")
    public ApiResponse orderReassignment(Long orderId){
        try {
            return ApiResponse.success(iOrderInfoService.orderReassignment(tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId(),orderId));
        } catch (Exception e) {
            logger.error("orderReassignment error",e);
        }
        return ApiResponse.error("订单管理订单改派失败");
    }

}
