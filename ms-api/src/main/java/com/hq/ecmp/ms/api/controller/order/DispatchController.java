package com.hq.ecmp.ms.api.controller.order;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hq.ecmp.mscore.dto.DispatchInfoDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
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


    @Resource
    private IOrderInfoService iOrderInfoService;

    @Resource
    private IDispatchService dispatchService;



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
    public ApiResponse<DispatchOrderInfo> detail(Long orderId) {
    	return ApiResponse.success(iOrderInfoService.getCompleteDispatchOrderDetailInfo(orderId));
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
    public ApiResponse rejectReassign(@RequestParam Long orderId,
                                @RequestParam String rejectReason,
                                @RequestParam Long optUserId) {
    	boolean rejectReassignFlag = iOrderInfoService.rejectReassign(orderId, rejectReason, optUserId);
    	if(rejectReassignFlag){
    		return ApiResponse.success();
    	}
    	return ApiResponse.error();
    }

    @ApiOperation(value = "ownCarSendCar", notes = "自有车派车", httpMethod = "POST")
    @PostMapping("/ownCarSendCar")
    public ApiResponse ownCarSendCar(Long orderId,Long driverId,Long carId,Long optUserId) {

         boolean ownCarSendCar = iOrderInfoService.ownCarSendCar(orderId, driverId, carId, optUserId);
         if(ownCarSendCar){
        	 return ApiResponse.success();
         }else{
        	 return ApiResponse.error("调派单【"+orderId+"】自有车派车失败");
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
