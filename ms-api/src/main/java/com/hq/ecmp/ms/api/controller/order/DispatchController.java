package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.DispatchSendCarPageInfo;
import com.hq.ecmp.mscore.dto.dispatch.*;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 后管-调度控制器
 * @author cm
 *
 */
@RestController
@RequestMapping("/dispatch")
@Slf4j
public class DispatchController {


    @Autowired
    @Lazy
    private IOrderInfoService iOrderInfoService;

    @Autowired
    @Lazy
    private OrderInfoTwoService orderInfoTwoService;
    @Autowired
    private IDispatchService dispatchService;

    @Autowired
    private TokenService tokenService;



    @ApiOperation(value = "getApplyDispatchList", notes = "获取申请调度列表 ", httpMethod = "POST")
    @PostMapping("/getApplyDispatchList")
    public ApiResponse<PageResult<ApplyDispatchVo>> getUserDispatchedOrder(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        query.setCompanyId(loginUser.getUser().getDept().getCompanyId());
        List<ApplyDispatchVo> list = iOrderInfoService.queryApplyDispatchList(query);
    	Integer totalNum = iOrderInfoService.queryApplyDispatchListCount(query);
    	PageResult<ApplyDispatchVo> pageResult = new PageResult<ApplyDispatchVo>(Long.valueOf(totalNum), list);
        return ApiResponse.success(pageResult);
    }


    @Log(title = "车辆调度",content = "获取系统已经完成调派或已过期的订单详细信息(包含申请和改派的)", businessType = BusinessType.OTHER)
    @ApiOperation(value = "detail", notes = "获取系统已经完成调派或已过期的订单详细信息(包含申请和改派的) ", httpMethod = "POST")
    @PostMapping("/detail")
    public ApiResponse<DispatchSendCarPageInfo> detail(@RequestBody String orderId) {
    	return ApiResponse.success(iOrderInfoService.getUserDispatchedOrder(Long.valueOf(orderId)));
    }

    @Log(title = "车辆调度",content = "派车详情页(包含申请和改派的)",businessType = BusinessType.OTHER)
    @ApiOperation(value = "sendDetail", notes = "派车详情页(包含申请和改派的)", httpMethod = "POST")
    @PostMapping("/sendDetail")
    public ApiResponse<DispatchSendCarPageInfo> sendDetail(@RequestBody Long orderId) {
    	return ApiResponse.success(iOrderInfoService.getDispatchSendCarPageInfo(orderId));
    }


    @ApiOperation(value = "getReassignmentDispatchList", notes = "获取改派列表 ", httpMethod = "POST")
    @PostMapping("/getReassignmentDispatchList")
    public ApiResponse<PageResult<ApplyDispatchVo>> getReassignmentDispatchList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        query.setCompanyId(loginUser.getUser().getOwnerCompany());
        List<ApplyDispatchVo> list = iOrderInfoService.queryReassignmentDispatchList(query);
    	Integer totalNum = iOrderInfoService.queryReassignmentDispatchListCount(query);
    	PageResult<ApplyDispatchVo> pageResult = new PageResult<ApplyDispatchVo>(Long.valueOf(totalNum), list);
        return ApiResponse.success(pageResult);
    }

    @Log(title = "车辆调度:改派订单-驳回", businessType = BusinessType.OTHER)
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

    @Log(title = "车辆调度:自有车派车", businessType = BusinessType.OTHER)
	@ApiOperation(value = "ownCarSendCar", notes = "自有车派车", httpMethod = "POST")
	@PostMapping("/ownCarSendCar")
	public ApiResponse ownCarSendCar(String OrderNo, String driverId, String carId) {
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		try {
			boolean ownCarSendCar = iOrderInfoService.ownCarSendCar(Long.valueOf(OrderNo), Long.valueOf(driverId),
					Long.valueOf(carId), loginUser.getUser().getUserId());
			if (ownCarSendCar) {
				return ApiResponse.success();
			} else {
				return ApiResponse.error("调派单【" + OrderNo + "】自有车派车失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("调派单【" + OrderNo + "】自有车派车异常", e);
		}
	}

    /**
     *调度-获取可选择的车辆
     */
    @ApiOperation(value = "getWaitSelectedCars", notes = "调度 获取可选择的车辆", httpMethod = "POST")
    @PostMapping("/getWaitSelectedCars")
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(@RequestBody DispatchSelectCarDto dispatchSelectCarDto) {
        return dispatchService.getWaitSelectedCars(dispatchSelectCarDto);
    }

    /**
     *调度-锁定 被选中的车辆，防止为其他司机选择
     */
    @ApiOperation(value = "lockSelectedCar", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/lockSelectedCar")
    public ApiResponse<DispatchResultVo> lockSelectedCar(@RequestBody DispatchLockCarDto dispatchLockCarDto) {
        return  dispatchService.lockSelectedCar(dispatchLockCarDto);
    }

    /**
     *调度-锁定 被选中的车辆，防止为其他司机选择
     */
    @ApiOperation(value = "unlockSelectedCar", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/unlockSelectedCar")
    public ApiResponse unlockSelectedCar(@RequestBody DispatchLockCarDto dispatchLockCarDto) {
        return  dispatchService.unlockSelectedCar(dispatchLockCarDto);
    }

    /**
     *调度-获取可选择的司机
     */
    @ApiOperation(value = "getWaitSelectedDrivers", notes = "调度-获取可选择的司机", httpMethod = "POST")
    @PostMapping("/getWaitSelectedDrivers")
    public ApiResponse<DispatchResultVo> getWaitSelectedDrivers(@RequestBody DispatchSelectDriverDto dispatchSelectDriverDto) {
        return  dispatchService.getWaitSelectedDrivers(dispatchSelectDriverDto);
    }

    /**
     *调度-锁定 被选中的司机，防止为其他调度员选择
     */
    @ApiOperation(value = "lockSelectedDriver", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/lockSelectedDriver")
    public ApiResponse<DispatchResultVo> lockSelectedDriver(@RequestBody DispatchLockDriverDto dispatchLockDriverDto) {
        return  dispatchService.lockSelectedDriver(dispatchLockDriverDto);
    }

    /**
     *调度-解除锁定 被选中的司机
     */
    @ApiOperation(value = "unlockSelectedDriver", notes = "调度-锁定 被选中的车辆，防止为其他司机选择", httpMethod = "POST")
    @PostMapping("/unlockSelectedDriver")
    public ApiResponse unlockSelectedDriver(@RequestBody DispatchLockDriverDto dispatchLockDriverDto) {
        return  dispatchService.unlockSelectedDriver(dispatchLockDriverDto);
    }

    /**
     * 自动调度
     *
     * @param dispatchCountCarAndDriverDto dispatchCountCarAndDriverDto
     * @return ApiResponse<DispatchCountDriverAndOrderVo>
     */
    @ApiOperation(value = "autoDispatch", notes = "自动调度", httpMethod = "POST")
    @PostMapping("/autoDispatch")
    public ApiResponse<DispatchResultVo> autoDispatch(@RequestBody DispatchCountCarAndDriverDto dispatchCountCarAndDriverDto) {
        return  dispatchService.autoDispatch(dispatchCountCarAndDriverDto);
    }

    /**
     * 调度无车驳回操作
     * @param orderId  订单id
     * @param reason 驳回原因
     * @return
     */
    @ApiOperation(value = "调度无车驳回")
    @PostMapping("/noCarDenied")
    public ApiResponse noCarDenied(Long orderId,String reason){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            dispatchService.noCarDenied(orderId, reason, loginUser.getUser().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("调度无车驳回通过");
    }

    /**
     * 获取申请调度列表
     * @param query
     * @return
     */
    @ApiOperation(value = "queryDispatchList", notes = "获取申请调度列表 ", httpMethod = "POST")
    @PostMapping("/queryDispatchList")
    public ApiResponse<Map<String,Object>> queryDispatchList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            Map<String,Object> list = orderInfoTwoService.queryDispatchList(query,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }

    /**
     * 获取改派调度列表
     * @param query
     * @return
     */
    @ApiOperation(value = "queryDispatchReassignmentList", notes = "获取改派调度列表 ", httpMethod = "POST")
    @PostMapping("/queryDispatchReassignmentList")
    public ApiResponse<Map<String,Object>> queryDispatchReassignmentList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            Map<String,Object> list = orderInfoTwoService.queryDispatchReassignmentList(query,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }
    /**
     *获取直接调度列表
     * @param query
     * @return
     */
    @ApiOperation(value = "queryStraightDispatchList", notes = "获取直接调度列表 ", httpMethod = "POST")
    @PostMapping("/queryStraightDispatchList")
    public ApiResponse<Map<String,Object>> queryStraightDispatchList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            Map<String,Object> list = orderInfoTwoService.queryDispatchOrder(loginUser,query);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }
}
