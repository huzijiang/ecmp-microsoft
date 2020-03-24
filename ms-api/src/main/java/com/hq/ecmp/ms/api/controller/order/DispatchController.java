package com.hq.ecmp.ms.api.controller.order;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.dto.DispatchInfoDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.mscore.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * 后管-调度控制器
 * @author cm
 *
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchController {


    @Autowired
    private IOrderInfoService iOrderInfoService;

    @Autowired
    private IDispatchService dispatchService;
    
    @Autowired
    private TokenService tokenService;



    @ApiOperation(value = "getApplyDispatchList", notes = "获取申请调度列表 ", httpMethod = "POST")
    @PostMapping("/getApplyDispatchList")
    public ApiResponse<PageResult<ApplyDispatchVo>> getUserDispatchedOrder(ApplyDispatchQuery query){
    	List<ApplyDispatchVo> list = iOrderInfoService.queryApplyDispatchList(query);
    	Integer totalNum = iOrderInfoService.queryApplyDispatchListCount(query);
    	PageResult<ApplyDispatchVo> pageResult = new PageResult<ApplyDispatchVo>(Long.valueOf(totalNum), list);
        return ApiResponse.success(pageResult);
    }



    @ApiOperation(value = "detail", notes = "获取系统已经完成调派或已过期的订单详细信息(包含申请和改派的) ", httpMethod = "POST")
    @PostMapping("/detail")
    public ApiResponse<DispatchOrderInfo> detail(String orderId) {
    	return ApiResponse.success(iOrderInfoService.getCompleteDispatchOrderDetailInfo(Long.valueOf(orderId)));
    }


    @ApiOperation(value = "sendDetail", notes = "派车详情页(包含申请和改派的)", httpMethod = "POST")
    @PostMapping("/sendDetail")
    public ApiResponse<DispatchOrderInfo> sendDetail(Long orderId) {
    	return ApiResponse.success(iOrderInfoService.getWaitDispatchOrderDetailInfo(orderId));
    }


    @ApiOperation(value = "getReassignmentDispatchList", notes = "获取改派列表 ", httpMethod = "POST")
    @PostMapping("/getReassignmentDispatchList")
    public ApiResponse<PageResult<ApplyDispatchVo>> getReassignmentDispatchList(ApplyDispatchQuery query){
    	List<ApplyDispatchVo> list = iOrderInfoService.queryReassignmentDispatchList(query);
    	Integer totalNum = iOrderInfoService.queryReassignmentDispatchListCount(query);
    	PageResult<ApplyDispatchVo> pageResult = new PageResult<ApplyDispatchVo>(Long.valueOf(totalNum), list);
        return ApiResponse.success(pageResult);
    }


    @ApiOperation(value = "改派订单-驳回", httpMethod = "POST")
    @RequestMapping("/rejectReassign")
    public ApiResponse rejectReassign(@RequestParam String orderId,
                                @RequestParam String rejectReason) {
    	 HttpServletRequest request = ServletUtils.getRequest();
         LoginUser loginUser = tokenService.getLoginUser(request);
    	boolean rejectReassignFlag = iOrderInfoService.rejectReassign(Long.valueOf(orderId), rejectReason, loginUser.getUser().getUserId());
    	if(rejectReassignFlag){
    		return ApiResponse.success();
    	}
    	return ApiResponse.error();
    }

    @ApiOperation(value = "ownCarSendCar", notes = "自有车派车", httpMethod = "POST")
    @PostMapping("/ownCarSendCar")
    public ApiResponse ownCarSendCar(String OrderNo,String driverId,String carId) {
    	 HttpServletRequest request = ServletUtils.getRequest();
         LoginUser loginUser = tokenService.getLoginUser(request);
         boolean ownCarSendCar = iOrderInfoService.ownCarSendCar(Long.valueOf(OrderNo), Long.valueOf(driverId), Long.valueOf(carId), loginUser.getUser().getUserId());
         if(ownCarSendCar){
        	 return ApiResponse.success();
         }else{
        	 return ApiResponse.error("调派单【"+OrderNo+"】自有车派车失败");
         }
    }


    /**
     *调度-获取可选择的车辆
     */
    @ApiOperation(value = "getWaitSelectedCars", notes = "调度 获取可选择的车辆", httpMethod = "POST")
    @PostMapping("/getWaitSelectedCars")
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchInfoDto dispatchInfoDto) {

        return dispatchService.getWaitSelectedCars(dispatchInfoDto);
    }

    /**
     *调度-锁定 被选中的车辆，防止为其他司机选择
     */
    @ApiOperation(value = "lockSelectedCar", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/lockSelectedCar")
    public ApiResponse<DispatchResultVo> lockSelectedCar(DispatchInfoDto dispatchInfoDto) {

        return  dispatchService.lockSelectedCar(dispatchInfoDto);
    }

    /**
     *调度-获取可选择的司机
     */
    @ApiOperation(value = "getWaitSelectedDrivers", notes = "调度-获取可选择的司机", httpMethod = "POST")
    @PostMapping("/getWaitSelectedDrivers")
    public ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchInfoDto dispatchInfoDto) {

        return  dispatchService.getWaitSelectedDrivers(dispatchInfoDto);
    }

    /**
     *调度-锁定 被选中的车辆，防止为其他司机选择
     */
    @ApiOperation(value = "lockSelectedDriver", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/lockSelectedDriver")
    public ApiResponse<DispatchResultVo> lockSelectedDriver(DispatchInfoDto dispatchInfoDto) {

        return  dispatchService.lockSelectedDriver(dispatchInfoDto);
    }



}
