package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyUseWithTravelDto;
import com.hq.ecmp.mscore.dto.OrderDriverAppraiseDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.EnterpriseCarTypeInfoMapper;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private TokenService tokenService;

    @Resource
    @Lazy
    private IOrderInfoService iOrderInfoService;

    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Resource
    private DriverServiceAppraiseeInfoService driverServiceAppraiseeInfoService;
    @Resource
    private IDriverHeartbeatInfoService driverHeartbeatInfoService;

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;

    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;

    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;

    /**
     * 初始化订单-公务创建订单
     * 根据行程申请信息生成订单信息
     * 改变订单的状态为  初始化
     *
     * @param officialOrderReVo
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "公务创建订单",businessType = BusinessType.INSERT,operatorType = OperatorType.MOBILE)
    @Log(value = "公务创建订单")
    @ApiOperation(value = "公务创建订单", notes = "公务创建订单", httpMethod = "POST")
    @PostMapping("/officialOrder")
    public ApiResponse officialOrder(@RequestBody OfficialOrderReVo officialOrderReVo) {
        Long orderId;
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            officialOrderReVo.setCompanyId(loginUser.getUser().getOwnerCompany());
            orderId = iOrderInfoService.officialOrder(officialOrderReVo,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("公务下单成功",orderId);
    }

    /**
     * 用户请求调派订单
     * 改变订单的状态为  待派单
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "applyDispatchOrder", notes = "请求订单 调派车辆 ", httpMethod = "POST")
    @PostMapping("/applyDispatchOrder")
    public ApiResponse applyDispatchOrder(OrderDto orderDto) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.WAITINGLIST.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if (i != 1) {
                return ApiResponse.error("'订单状态改为【待派单失败】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("'订单状态改为【待派单失败】");
        }
        return ApiResponse.success("订单状态修改成功");
    }

    /**
     * 自动约车-向网约车平台发起约车请求，直到网约车平台回应 约到车为止，期间一直为约车中，约到后改变订单状态为 已派单
     * 改变订单的状态为  约车中
     * 需要留一个终止循环派单的 开关
     *
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "自动约车",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "自动约车")
    @ApiOperation(value = "letPlatCallTaxi", notes = "自动约车-向网约车平台发起约车请求 改变订单的状态为  约车中-->已派单", httpMethod = "POST")
    @PostMapping("/letPlatCallTaxi")
    public ApiResponse letPlatCallTaxi( @RequestParam("orderNo") String orderNo,
                                        @RequestParam("carLevel") String carLevel) {
        try {

            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            Long orderId = Long.parseLong(orderNo);
            OrderInfo orderInfoOld = iOrderInfoService.selectOrderInfoById(orderId);
            if(orderInfoOld.equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getPrState())){
                throw new Exception("网约车不支持包车");
            }
            iOrderInfoService.platCallTaxiParamValid(orderId,String.valueOf(userId),carLevel);
            //判断是否是网约车且是往返，如果是，超过约定时间则下一单返程的网约车订单
            iOrderInfoService.checkCreateReturnAuthority(orderId,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("约车成功");
    }


    /**
     * 调派订单  受到到订单的变化规则限制。此处为非网约车
     * 改变订单的状态为 已派单。
     * 如果是网约车  向网约车平台发起订单，等待返回后绑定
     * 如果是差旅，乘客需要自己向网约车发起订单，则该改变订单状态
     * 如果自有直接改变订单信息
     *
     * @param orderDto  行程申请信息
     * @param carDto    行程申请信息
     * @param driverDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "dispatchOrder", notes = "请求订单 调派车辆 ", httpMethod = "POST")
    @PostMapping("/dispatchOrder")
    public ApiResponse dispatchOrder(OrderDto orderDto, CarDto carDto, DriverDto driverDto) {

        return null;
    }


    /**
     * 司机到达出发地，准备开始服务
     * 改变订单的状态为 准备服务
     *
     * @param orderDto 行程申请信息
     * @param userDto  司机信息
     * @return
     */
    @ApiOperation(value = "readyServiceable", notes = "司机到达出发地，准备开始服务 ", httpMethod = "POST")
    @PostMapping("/readyServiceable")
    public ApiResponse readyServiceable(OrderDto orderDto, UserDto userDto) {
        return null;
    }


    /**
     * 司机完成订单
     * 改变订单的状态为 服务结束
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "finishOrder", notes = "司机完成订单 ", httpMethod = "POST")
    @PostMapping("/finishOrder")
    public ApiResponse finishOrder(OrderDto orderDto, UserDto userDto) {
        return null;
    }


    /**
     * 用户确认订单
     * 改变订单的状态为 订单关闭
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "用户确认订单",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "确认订单")
    @ApiOperation(value = "affirmOrder", notes = "用户确认订单 ", httpMethod = "POST")
    @PostMapping("/affirmOrder")
    public ApiResponse affirmOrder(@RequestBody OrderDto orderDto) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setUpdateBy(String.valueOf(userId));
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            int re = iOrderInfoService.updateOrderInfo(orderInfo);
            if (re != 1) {
                return ApiResponse.error("行程确认失败");
            }
            //插入订单轨迹表
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderDto.getOrderId()), OrderState.ORDERCLOSE.getState(), String.valueOf(userId),null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("行程确认失败");
        }
        return ApiResponse.success();
    }

    /**
     * 用户取消订单:
     * <p>
     * 逻辑：1.订单状态为未约到车的状态，直接更改订单状态为订单取消。
     * 2. 订单状态为约到车未服务的状态 《1》：如果是网约车，调用网约车取消订单接口，取消订单，然后修改订单状态为订单取消。
     * 《2》：如果是自有车，直接更改订单状态为订单取消，成功以后给司机发送订单取消的消息通知。
     * 3. 插入订单状态变化轨迹表数据
     * <p>
     * 改变订单的状态为 订单关闭
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "取消订单",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "取消订单")
    @ApiOperation(value = "cancelOrder", notes = "用户取消订单 ", httpMethod = "POST")
    @PostMapping("/cancelOrder")
    public ApiResponse cancelOrder(@RequestBody OrderDto orderDto) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            Long orderId = orderDto.getOrderId();
            iOrderInfoService.cancelOrder(orderId,userId,orderDto.getCancelReason());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("订单取消失败->" + e.getMessage());
        }
        return ApiResponse.success("订单取消成功");
    }


    /**
     * 获取所有等待 调度员 调派 的订单列表(包含改派订单)
     *
     * 自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的驾驶员能看到该订单
     *
     * 只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单
     * @param  userDto  调度员用户信息
     * @return
     */
    @ApiOperation(value = "getAllWaitDispatchOrder", notes = "获取所有等待 调度员 调派 的订单列表 ", httpMethod = "POST")
    @PostMapping("/getAllWaitDispatchOrder")
    public ApiResponse<List<DispatchOrderInfo>> getAllWaitDispatchOrder(UserDto userDto){
    	HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        return ApiResponse.success(iOrderInfoService.queryWaitDispatchList(userId));
    }

    /**
     * 获取调派员已经完成调派的订单列表
     *
     * @param userDto 调度员用户信息
     * @return
     */
    @ApiOperation(value = "getUserDispatchedOrder", notes = "获取已经完成调派的订单列表 ", httpMethod = "POST")
    @PostMapping("/getUserDispatchedOrder")
    public ApiResponse<List<DispatchOrderInfo>> getUserDispatchedOrder(UserDto userDto){
        //获取当前登陆用户的信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //获取当前登陆用户的信息Id
        Long userId = loginUser.getUser().getUserId();
        return ApiResponse.success(iOrderInfoService.queryCompleteDispatchOrder(userId));
    }

    /**
     * 获取系统已经完成调派的订单详细信息(包含改派的)
     *
     * @return
     */
    @ApiOperation(value = "getCompleteDispatchOrderDetailInfo", notes = "获取系统所有已经完成调派的订单详细信息 ", httpMethod = "POST")
    @PostMapping("/getCompleteDispatchOrderDetailInfo")
    public ApiResponse<DispatchOrderInfo> getCompleteDispatchOrderDetailInfo(Long orderId) {
    	return ApiResponse.success(iOrderInfoService.getCompleteDispatchOrderDetailInfo(orderId));
    }

    /**
     * 获取等待调度的订单详细信息(包含待改派的)
     *
     * @param orderId 订单信息
     * @return
     */
    @ApiOperation(value = "getWaitDispatchOrderDetailInfo", notes = "获取等待调度的订单详细信息", httpMethod = "POST")
    @PostMapping("/getWaitDispatchOrderDetailInfo")
    public ApiResponse<DispatchOrderInfo> getWaitDispatchOrderDetailInfo(Long orderId) {
        //获取当前登陆用户的信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //获取当前登陆用户的信息Id
        Long userId = loginUser.getUser().getUserId();
        ApiResponse<DispatchOrderInfo>  dispatchOrderInfo =iOrderInfoService.getWaitDispatchOrderDetailInfo(orderId,userId);
        return dispatchOrderInfo;
    }


    /**
     * 自有车派车
     * @param orderId
     * @param driverId
     * @param carId
     * @return
     */
	@ApiOperation(value = "", notes = "自有车派车", httpMethod = "POST")
	@PostMapping("/ownCarSendCar")
	public ApiResponse ownCarSendCar(Long orderId, Long driverId, Long carId) {
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		try {
			boolean ownCarSendCar = iOrderInfoService.ownCarSendCar(orderId, driverId, carId, userId);
			if (ownCarSendCar) {
				return ApiResponse.success();
			} else {
				return ApiResponse.error("调派单【" + orderId + "】自有车派车失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("调派单【" + orderId + "】自有车派车异常", e);
		}

	}

	/**
     * 调度 选择了自有车后生成行程预估价
     * @param orderId
     * @return
     */
	@ApiOperation(value = "sendCarBeforeCreatePlanPrice", notes = "调度 选择了自有车后生成行程预估价", httpMethod = "POST")
	@PostMapping("/sendCarBeforeCreatePlanPrice")
	public ApiResponse sendCarBeforeCreatePlanPrice(Long orderId) {
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		try {
			boolean ownCarSendCar = iOrderInfoService.sendCarBeforeCreatePlanPrice(orderId,userId);
			if (ownCarSendCar) {
				return ApiResponse.success();
			} else {
				return ApiResponse.error("调派单【" + orderId + "】自有车派车前生成行程预估价失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("调派单【" + orderId + "】自有车派车前生成行程预估价异常", e);
		}

	}


    /**
     * 评价 订单
     *
     * @param orderAppraiseDto 评价信息
     * @return
     */
    @ApiOperation(value = "appraiseOrder", notes = "评价 订单 ", httpMethod = "POST")
    @PostMapping("/appraiseOrder")
    public ApiResponse appraiseOrder(OrderAppraiseDto orderAppraiseDto) {

        return null;
    }

    /**
     * @return com.hq.common.core.api.ApiResponse
     * @author yj
     * @Description //TODO 获取乘客订单列表
     * @Date 10:11 2020/3/4
     * @Param []
     **/
    @com.hq.core.aspectj.lang.annotation.Log(title = "我的行程列表",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "我的行程列表")
    @ApiOperation(value = "我的行程订单列表", httpMethod = "POST")
    @RequestMapping("/getOrderList")
    public ApiResponse<List<OrderListInfo>> getIncompleteOrderList(@RequestBody PageRequest orderPage) {
        List<OrderListInfo> orderList;
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            orderList = iOrderInfoService.getOrderList(userId, orderPage.getPageNum(), orderPage.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("加载订单列表失败");
        }
        return ApiResponse.success(orderList);
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "改派订单",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "改派订单")
    @ApiOperation(value = "改派订单", httpMethod = "POST")
    @RequestMapping("/reassign")
    public ApiResponse reassign(@RequestParam(value = "orderNo") String orderNo,
                                @RequestParam(value = "rejectReason") String rejectReason,
                                @RequestParam(value = "status") String status) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            iOrderInfoService.reassign(orderNo,rejectReason,status,userId,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success();

    }

    /**
    *   @author caobj
    *   @Description  获取个人中心当前司机的所有任务列表(已完成/未完成)
    *   @Date 14:03 2020/3/11
    *   @Param  [driverListRequest]
    *   @return com.hq.common.core.api.ApiResponse<java.util.List<com.hq.ecmp.mscore.domain.OrderDriverListInfo>>
    **/
    @ApiOperation(value = "driverOrderList", notes = "获取司机的我的任务列表")
    @RequestMapping("/driverOrderList")
    public ApiResponse<PageResult<OrderDriverListInfo>> driverOrderList(@RequestBody PageRequest driverListRequest) {
        List<OrderDriverListInfo> driverOrderList = null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            driverOrderList = iOrderInfoService.getDriverOrderList(loginUser, driverListRequest.getPageNum(), driverListRequest.getPageSize());
            Integer count=iOrderInfoService.getDriverOrderListCount(loginUser);
            return ApiResponse.success(new PageResult<OrderDriverListInfo>(Long.valueOf(count),driverOrderList));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     *   @author caobj
     *   @Description  获取首页当前司机的所有未完成任务列表
     *   @Date 14:03 2020/3/11
     *   @Param  [driverListRequest]
     *   @return com.hq.common.core.api.ApiResponse<java.util.List<com.hq.ecmp.mscore.domain.OrderDriverListInfo>>
     **/
    @ApiOperation(value = "driverOrderUndoneList", notes = "获取首页当前司机的所有未完成任务列表")
    @RequestMapping("/driverOrderUndoneList")
    public ApiResponse<List<OrderDriverListInfo>> driverOrderUndoneList(@RequestBody PageRequest driverListRequest) {
        List<OrderDriverListInfo> driverOrderList = null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            driverOrderList = iOrderInfoService.driverOrderUndoneList(loginUser, driverListRequest.getPageNum(), driverListRequest.getPageSize(),driverListRequest.getDay());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success(driverOrderList);
    }

    @ApiOperation(value = "driverOrderCount", notes = "获取未完成的任务数量")
    @RequestMapping("/driverOrderCount")
    public ApiResponse<Integer> driverOrderCount() {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            int count = iOrderInfoService.driverOrderCount(loginUser);
            return ApiResponse.success(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
    *   @author yj
    *   @Description  //TODO  差旅申请派车(网约车 下单+约车 包含自有车  下单)
    *   @Date 14:02 2020/3/7
    *   @Param  [applyUseWithTravelDto]
    *   @return com.hq.common.core.api.ApiResponse
    **/
    @com.hq.core.aspectj.lang.annotation.Log(title = "差旅下单",businessType = BusinessType.INSERT,operatorType = OperatorType.MOBILE)
    @Log(value = "差旅下单")
    @ApiOperation(value = "差旅下单接口",notes = "")
    @RequestMapping("/travelOrder")
    public ApiResponse travelOrder(@RequestBody ApplyUseWithTravelDto applyUseWithTravelDto){
        Long orderId;
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            orderId = iOrderInfoService.applyUseCarWithTravel(applyUseWithTravelDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("申请派车失败");
        }
        return ApiResponse.success("申请派车成功",orderId);
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "驾驶员评价",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log(value = "驾驶员评价")
    @ApiOperation(value = "驾驶员评价接口")
    @PostMapping("/orderDriverAppraise")
    public ApiResponse orderDriverAppraise(@RequestBody  OrderDriverAppraiseDto orderDriverAppraiseDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            DriverServiceAppraiseeInfo driverServiceAppraiseeInfo = new DriverServiceAppraiseeInfo();
            driverServiceAppraiseeInfo.setCarId(orderDriverAppraiseDto.getCardId());
            driverServiceAppraiseeInfo.setContent(orderDriverAppraiseDto.getContent());
            driverServiceAppraiseeInfo.setDriverId(orderDriverAppraiseDto.getDriverId());
            driverServiceAppraiseeInfo.setScore(Double.parseDouble(orderDriverAppraiseDto.getScore()));
            driverServiceAppraiseeInfo.setOrderId(orderDriverAppraiseDto.getOrderId());
            driverServiceAppraiseeInfo.setItem(orderDriverAppraiseDto.getItem());
            driverServiceAppraiseeInfo.setCreateBy(userId);
            driverServiceAppraiseeInfo.setUpdateBy(null);
            driverServiceAppraiseeInfo.setUpdateTime(null);
            OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(orderDriverAppraiseDto.getOrderId());
            driverServiceAppraiseeInfo.setCarLicense(orderInfo.getCarLicense());
            driverServiceAppraiseeInfoService.insert(driverServiceAppraiseeInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("评价失败");
        }
        return ApiResponse.success("评价成功");
    }
    /**
     *   @author caobj
     *   @Description 乘客端获取订单详情
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "乘客端获取订单详情",httpMethod = "POST")
    @RequestMapping("/orderBeServiceDetail")
    public ApiResponse<OrderVO> orderBeServiceDetail(String flag,String orderId) {
        try {
            OrderVO orderVO = iOrderInfoService.orderBeServiceDetail(Long.parseLong(orderId));
            return ApiResponse.success(orderVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }
    /**
     *   @author caobj
     *   @Description 获取订单状态
     *   @Date 10:11 2020/3/4
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "获取订单状态",httpMethod = "POST")
    @RequestMapping("/getOrderState")
    public ApiResponse<OrderStateVO> getOrderState(String flag,String orderId){
        log.info("获取订单状态:订单号:"+orderId);
        Long orderNo=Long.parseLong(orderId);
        try {
            OrderStateVO  orderVO = iOrderInfoService.getOrderState(orderNo);
            //TODO 记得生产放开
            if (CarConstant.USR_CARD_MODE_HAVE.equals(orderVO.getUseCarMode())){//自有车
                DriverHeartbeatInfo driverHeartbeatInfo = driverHeartbeatInfoService.findNowLocation(orderVO.getDriverId(), orderNo);
                if(driverHeartbeatInfo!=null){
                    String latitude=driverHeartbeatInfo.getLatitude().stripTrailingZeros().toPlainString();
                    String longitude=driverHeartbeatInfo.getLongitude().stripTrailingZeros().toPlainString();
                    orderVO.setDriverLongitude(longitude);
                    orderVO.setDriverLatitude(latitude);
                }
            }else {
                orderVO = iOrderInfoService.getTaxiState(orderVO, orderNo);
            }
            return ApiResponse.success(orderVO);
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error(e.getMessage());
        }
    }

    /**
     *   @author caobj
     *   @Description 司机端获取任务详情
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "司机端获取任务详情",httpMethod = "POST")
    @RequestMapping("/driverOrderDetail")
    public ApiResponse<DriverOrderInfoVO> driverOrderDetail(@RequestBody OrderDto orderDto){
        try {
            DriverOrderInfoVO  orderVO = iOrderInfoService.driverOrderDetail(orderDto.getOrderId());
            return ApiResponse.success(orderVO);
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("司机端获取任务详情");
        }
    }

    /**
     *   @author caobj
     *   @Description 轮询获取提示语
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "轮询获取提示语",httpMethod = "POST")
    @RequestMapping("/getOrderHint")
    public ApiResponse<String> getOrderHint(@RequestBody OrderDto orderDto){
        try {
            String res = iOrderInfoService.orderHint(orderDto.getOrderId());
            return ApiResponse.success("获取成功",res);
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("获取提示语异常!");
        }
    }

    /**
     *   @author caobj
     *   @Description 轮询获取提示语
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "行程分享",httpMethod = "POST")
    @RequestMapping("/orderShare")
    public ApiResponse<String> orderShare(@RequestBody OrderDto orderDto){
        String url="";
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            String param="orderId="+orderDto.getOrderId()+"&userId="+userId+"&flag=share";
            return ApiResponse.success("分享成功",URLEncoder.encode(url+param, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("获取提示语异常!");
        }
    }

    @ApiOperation(value = "更换车辆",httpMethod = "POST")
    @RequestMapping("/replaceCar")
    public ApiResponse<DriverOrderInfoVO> replaceCar(@RequestBody OrderDto orderDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setCarId(orderDto.getCarId());
            iOrderInfoService.replaceCar(orderInfo,loginUser.getUser()==null?null:loginUser.getUser().getUserId());
            return ApiResponse.success();
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("更换车辆失败");
        }
    }
}
