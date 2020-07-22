package com.hq.ecmp.ms.api.controller.order;

import com.google.common.collect.Maps;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.SmsTemplateConstant;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.ms.api.dto.order.OrderUseTimeDto;
import com.hq.ecmp.mscore.bo.OrderFullInfoBo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyUseWithTravelDto;
import com.hq.ecmp.mscore.dto.OrderDriverAppraiseDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.netty.handler.codec.compression.FastLzFrameEncoder;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class  OrderController {

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

    @Resource
    private  OrderInfoTwoService orderInfoTwoService;

    @Resource
    private IJourneyInfoService journeyInfoService;

    @Resource
    private IJourneyPlanPriceInfoService journeyPlanPriceInfoService;

    @Resource
    private ISmsTemplateInfoService smsTemplateInfoService;

    @Resource
    private IOrderDispatcheDetailInfoService orderDispatcheDetailInfoService;

    @Resource
    private IJourneyPassengerInfoService journeyPassengerInfoService;

    @Resource
    private IEcmpUserService ecmpUserService;

    @Autowired
    private com.hq.ecmp.mscore.service.IDriverInfoService driverInfoService;

    @Autowired
    private  IJourneyNodeInfoService journeyNodeInfoService;

    @Autowired
    private IOrderAddressInfoService orderAddressInfoService;


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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
			log.error("业务处理异常", e);
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
			log.error("业务处理异常", e);
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
    public ApiResponse<PageResult<OrderListInfo>> getIncompleteOrderList(@RequestBody PageRequest orderPage) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            return ApiResponse.success(iOrderInfoService.getOrderList(loginUser.getUser(), orderPage.getPageNum(), orderPage.getPageSize(),orderPage.getIsConfirmState()));
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        return ApiResponse.success("加载订单列表失败");
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
            log.error("业务处理异常", e);
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
            driverOrderList = orderInfoTwoService.getDriverOrderList(loginUser, driverListRequest.getPageNum(), driverListRequest.getPageSize());
            Integer count=orderInfoTwoService.getDriverOrderListCount(loginUser);
            return ApiResponse.success(new PageResult<OrderDriverListInfo>(Long.valueOf(count),driverOrderList));
        } catch (Exception e) {
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            Long companyId = loginUser.getUser().getOwnerCompany();
            applyUseWithTravelDto.setCompanyId(companyId);
            orderId = iOrderInfoService.applyUseCarWithTravel(applyUseWithTravelDto,userId);
        } catch (Exception e) {
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     *   @author caobj
     *   @Description 乘客端佛山订单详情辅助页面
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "乘客端获取订单详情",httpMethod = "POST")
    @RequestMapping("/getOrderInfoDetail")
    public ApiResponse<UserApplySingleVo> getOrderInfoDetail(@RequestParam(value = "orderId",required = false) Long orderId,@RequestParam(value = "applyId",required = false) Long applyId) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            UserApplySingleVo orderVO = orderInfoTwoService.getOrderInfoDetail(orderId,loginUser.getUser(),applyId);
            return ApiResponse.success(orderVO);
        } catch (Exception e) {
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            log.error("业务处理异常", e);
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
            orderInfoTwoService.replaceCar(orderInfo,loginUser.getUser()==null?null:loginUser.getUser().getUserId());
            return ApiResponse.success();
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("更换车辆失败");
        }
    }
    /***
     * add by liuzb (一键报警获取当前订单乘车信息)
     * @param orderId
     * @return
     */
    @ApiOperation(value = "获取乘车信息",httpMethod = "POST")
    @RequestMapping("/getCarMessage")
    public ApiResponse<OrderInfoMessage> getCarMessage(Long orderId){
        try {
            return ApiResponse.success(iOrderInfoService.getMessage(orderId));
        }catch (Exception e){
            logger.error("获取乘车信息异常");
        }
        return  ApiResponse.error("获取乘车信息失败");
    }

    @ApiOperation(value = "取消订单",httpMethod = "POST")
    @RequestMapping("/cancelBusinessOrder")
    public ApiResponse<CancelOrderCostVO> cancelBusinessOrder(@RequestBody OrderDto orderDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            CancelOrderCostVO cancelOrderCostVO = orderInfoTwoService.cancelBusinessOrder(orderDto.getOrderId(), orderDto.getCancelReason(),loginUser.getUser().getUserId());
            return ApiResponse.success(cancelOrderCostVO);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("取消订单失败");
        }
    }


    @ApiOperation(value = "正在进行中的订单(乘客端卡片)",httpMethod = "POST")
    @RequestMapping("/runningOrder")
    public ApiResponse<List<RunningOrderVo>> runningOrder(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);

            List<RunningOrderVo> runningOrders = orderInfoTwoService.runningOrder(loginUser.getUser().getUserId());
            return ApiResponse.success(runningOrders);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("查询失败");
        }
    }

    @ApiOperation(value = "获取订单下载信息",httpMethod = "POST")
    @RequestMapping("/getDownOrderInfo")
    public ApiResponse<Map> getDownOrderInfo(Long orderId){
        try {
            Map<String,String> orderInfo = iOrderInfoService.downloadOrderData(orderId);
            return ApiResponse.success(orderInfo);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("查询失败");
        }
    }

    @ApiOperation(value = "统计内外部车队订单数",httpMethod = "POST")
    @PostMapping(value = "/selectOrderCarGroup")
    public ApiResponse<Map> selectOrderCarGroup(){
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        try {
            Map map = iOrderInfoService.selectOrderCarGroup(companyId);
            return ApiResponse.success(map);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("查询失败");
        }
    }
    @ApiOperation(value = "订单预约时段统计",httpMethod = "POST")
    @PostMapping(value = "/selectNormalOrderReserveTime")
    public ApiResponse<Map> selectNormalOrderReserveTime(@RequestBody StatisticsParam statisticsParam){
        try {
            Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
            Map map = iOrderInfoService.selectNormalOrderReserveTime(companyId,statisticsParam.getBeginDate(),statisticsParam.getEndDate());
            return ApiResponse.success(map);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("查询失败");
        }
    }

    /**
     * 更新订单用车时长
     * @param orderUseTimeDto
     * @return
     */
    @ApiOperation(value = "更新订单用车时长",httpMethod = "POST")
    @PostMapping(value = "/updateOrderUserCarTime")
    public ApiResponse updateOrderUserCarTime(@RequestBody OrderUseTimeDto orderUseTimeDto){
        OrderFullInfoBo orderFullInfoBo=null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //维护订单数据
            orderFullInfoBo=updateOrderUserCarTimeTransaction(orderUseTimeDto);
        }catch (Exception e){
            log.error("更新订单用车时长业务处理异常:"+orderUseTimeDto.getOrderNumber(), e);
            return  ApiResponse.error(e.getMessage());
        }

        try{
            //发送短信,查找订单相关信息
            Date st=orderFullInfoBo.getJourneyPlanPriceInfos().get(0).getPlannedDepartureTime();
            String startTime= DateUtils.formatDate(st,DateUtils.YYYY_MM_DD_HH_MM_SS) +"";
            String orderNumber=orderFullInfoBo.getOrderInfo().getOrderNumber()+"";
            String oldUseTime=orderFullInfoBo.getJourneyInfo().getOldUseTime()+"";
            String useTime=orderFullInfoBo.getJourneyInfo().getUseTime()+"";


            String userInfo="";
            String applyerInfo="";

            String dispatcherInfoInner="";
            String dispatcherInfoOuter="";

            //用车人信息
            JourneyPassengerInfo journeyPassengerInfo=new JourneyPassengerInfo();
                                 journeyPassengerInfo.setJourneyId(orderFullInfoBo.getJourneyInfo().getJourneyId());
            List<JourneyPassengerInfo> journeyPassengerInfos=journeyPassengerInfoService.selectJourneyPassengerInfoList(journeyPassengerInfo);
            if(journeyPassengerInfos.size()!=1){
                throw new Exception("行程用车人信息异常");
            }
            journeyPassengerInfo=journeyPassengerInfos.get(0);
            userInfo=journeyPassengerInfo.getName()+" "+journeyPassengerInfo.getMobile();

            //申请人信息
            EcmpUser userApplyer=null;
            if(StringUtils.isNotEmpty(orderFullInfoBo.getJourneyInfo().getUserId()+"")){
                userApplyer=ecmpUserService.selectEcmpUserById(orderFullInfoBo.getJourneyInfo().getUserId());
                applyerInfo=userApplyer.getNickName()+" "+userApplyer.getPhonenumber();
            }

            //调度信息查询
            OrderDispatcheDetailInfo orderDispatcheDetailInfo=new OrderDispatcheDetailInfo();
                                     orderDispatcheDetailInfo.setOrderId(orderFullInfoBo.getOrderInfo().getOrderId());
            List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos=orderDispatcheDetailInfoService.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo);
            if(orderDispatcheDetailInfos.size()!=1){
                throw new Exception("订单调度信息异常.");
            }

            Map<String, String> mapDispatcher = Maps.newHashMap();
            mapDispatcher.put("startTime", startTime);
            mapDispatcher.put("orderNumber", orderNumber);
            mapDispatcher.put("oldUseTime", oldUseTime);
            mapDispatcher.put("useTime", useTime);
            mapDispatcher.put("userInfo", userInfo);

            orderDispatcheDetailInfo=orderDispatcheDetailInfos.get(0);
            if(StringUtils.isNotEmpty(orderDispatcheDetailInfo.getInnerDispatcher()+"")){
                EcmpUser userInnerDispatcher=ecmpUserService.selectEcmpUserById(orderDispatcheDetailInfo.getInnerDispatcher());
                dispatcherInfoInner=userInnerDispatcher.getNickName()+" "+userInnerDispatcher.getPhonenumber();
                mapDispatcher.put("dispatcherInfo", dispatcherInfoInner);
                smsTemplateInfoService.sendSms(SmsTemplateConstant.UPDATE_ORDER_USE_TIME_FOR_DISPATCHER, mapDispatcher, userInnerDispatcher.getPhonenumber());
            }else {
                throw new Exception("订单调度信息异常.无内部调度信息");
            }

            if(StringUtils.isNotEmpty(orderDispatcheDetailInfo.getOuterDispatcher()+"")){
                EcmpUser userOuterDispatcher=ecmpUserService.selectEcmpUserById(orderDispatcheDetailInfo.getOuterDispatcher());
                dispatcherInfoOuter=userOuterDispatcher.getNickName()+" "+userOuterDispatcher.getPhonenumber();
                mapDispatcher.put("dispatcherInfo", dispatcherInfoOuter);
                smsTemplateInfoService.sendSms(SmsTemplateConstant.UPDATE_ORDER_USE_TIME_FOR_DISPATCHER, mapDispatcher, userOuterDispatcher.getPhonenumber());
            }

            Map<String, String> mapUser = Maps.newHashMap();
            mapUser.put("orderNumber", orderNumber);
            mapUser.put("startTime", startTime);
            mapUser.put("oldUseTime", oldUseTime);
            mapUser.put("useTime", useTime);
            mapUser.put("dispatcherInfo", dispatcherInfoInner);
            smsTemplateInfoService.sendSms(SmsTemplateConstant.UPDATE_ORDER_USE_TIME_FOR_USER, mapUser, journeyPassengerInfo.getMobile());


            Map<String, String> mapApplyer = Maps.newHashMap();
            mapApplyer.put("startTime", startTime);
            mapApplyer.put("orderNumber", orderNumber);
            mapApplyer.put("oldUseTime", oldUseTime);
            mapApplyer.put("useTime", useTime);
            mapApplyer.put("dispatcherInfo", dispatcherInfoInner);
            smsTemplateInfoService.sendSms(SmsTemplateConstant.UPDATE_ORDER_USE_TIME_FOR_APPLYER, mapApplyer,userApplyer.getPhonenumber());

            //向司机发送短信
            if(StringUtils.isNotEmpty(orderFullInfoBo.getOrderInfo().getDriverId()+"")){
                DriverInfo  driverInfo=new DriverInfo();
                            driverInfo=driverInfoService.selectDriverInfoById(orderFullInfoBo.getOrderInfo().getDriverId());
                Map<String, String> mapDriver = Maps.newHashMap();
                mapApplyer.put("startTime", startTime);
                mapApplyer.put("orderNumber", orderNumber);
                mapApplyer.put("oldUseTime", oldUseTime);
                mapApplyer.put("useTime", useTime);
                mapApplyer.put("userInfo", userInfo);
                mapApplyer.put("dispatcherInfo", dispatcherInfoInner);
                smsTemplateInfoService.sendSms(SmsTemplateConstant.UPDATE_ORDER_USE_TIME_FOR_DRIVER, mapApplyer,driverInfo.getMobile());
            }
        }catch (Exception e){
            log.error("订单号：{} 更新订单用车时长成功但发送短信失败,{}",orderUseTimeDto.getOrderNumber(),e.getMessage());
            return  ApiResponse.success("更新订单用车时长成功但发送短信失败。");
        }
        return  ApiResponse.success("更改订单用车时间成功。");
    }



    @Transactional
    public OrderFullInfoBo updateOrderUserCarTimeTransaction(OrderUseTimeDto orderUseTimeDto) throws  Exception{
        OrderFullInfoBo orderFullInfoBo=new OrderFullInfoBo();

        Double  newUserTime=0.0;
        try{
            newUserTime=Double.parseDouble(orderUseTimeDto.getNewUseTime());
        }catch (Exception e){
            throw new Exception("新的用车时间输入不合法.");
        }

        //订单状态检测,订单状态
        //必须在订单结束之前，来源于订单状态轨迹表
        OrderInfo   orderInfo=new OrderInfo();
        orderInfo.setOrderId(orderUseTimeDto.getOrderId());
        orderInfo.setOrderNumber(orderUseTimeDto.getOrderNumber());

        String state=iOrderInfoService.getOrderStateByOrderInfo(orderInfo);

        if(StringUtils.isEmpty(state)){
            throw new Exception("订单状态不符合要求。请检查订单是否已经结束.");
        }

        //查询订单信息
        orderInfo=iOrderInfoService.selectOrderInfoById(orderUseTimeDto.getOrderId());
        if(newUserTime<1.0){
            orderInfo.setItIsSupplement("M005");
        }else if(newUserTime>1.0){
            orderInfo.setItIsSupplement("M009");
        }else {
            orderInfo.setItIsSupplement("M001");
        }
        orderFullInfoBo.setOrderInfo(orderInfo);

        //更改行程信息,修改旧的用车时间  和新的用车时间，时间需要估算
        //查询行程信息
        JourneyInfo journeyInfo=new JourneyInfo();
                    journeyInfo=journeyInfoService.selectJourneyInfoById(orderInfo.getJourneyId());

        Double oldUseTime=Double.parseDouble(journeyInfo.getUseTime());

        if(newUserTime>=oldUseTime || newUserTime<=1.0){
            throw new Exception("订单用车时间不可延长且整日租、半日租不可修改。请确认正确的用车时间.");
        }

        //查询预算价信息
        JourneyPlanPriceInfo journeyPlanPriceInfo=new JourneyPlanPriceInfo();
        journeyPlanPriceInfo.setJourneyId(journeyInfo.getJourneyId());
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos=journeyPlanPriceInfoService.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
        if(journeyPlanPriceInfos.isEmpty()){
            throw new Exception("没有找到对应的预算价信息.");
        }
        if(journeyPlanPriceInfos.size()>1){
            throw new Exception("包车预算价信息生成错误.");
        }
        //根据天数算出分钟数。一天按照 24小时计算,可能为负数
        JourneyPlanPriceInfo  journeyPlanPriceInfoa=journeyPlanPriceInfos.get(0);

        int minutes=(int)((newUserTime-oldUseTime)*24*60);
        Calendar arrivalCalendar = Calendar.getInstance();
        arrivalCalendar.setTime(journeyPlanPriceInfoa.getPlannedArrivalTime());
        arrivalCalendar.add(Calendar.MINUTE, minutes);

        Date arrivalCalendarDate=arrivalCalendar.getTime();

        //如果小于当前时间。则操作失败
        if(arrivalCalendar.getTime().getTime() < System.currentTimeMillis()){
            throw new Exception("更改后的使用天数输入 不能小于 当前已使用天数.");
        }
        //更改行程到达时间
        journeyInfo.setOldUseTime(oldUseTime.toString());
        journeyInfo.setUseTime(newUserTime.toString());
        journeyInfo.setEndDate(arrivalCalendarDate);

        if(newUserTime<1.0){
            journeyInfo.setCharterCarType("T001");
        }else if(newUserTime>1.0){
            journeyInfo.setCharterCarType("T009");
        }else {
            journeyInfo.setCharterCarType("T002");
        }
        int i=journeyInfoService.updateJourneyInfo(journeyInfo);
        if(i!=1){
            throw new Exception("行程信息更新失败.");
        }
        orderFullInfoBo.setJourneyInfo(journeyInfo);

        //更改预算价到达时间
        journeyPlanPriceInfoa.setPlannedArrivalTime(arrivalCalendarDate);
        int j=journeyPlanPriceInfoService.updateJourneyPlanPriceInfo(journeyPlanPriceInfoa);
        if(j!=1){
            throw new Exception("行程预算价信息更新失败.");
        }

        //更高行程节点时间
        List<JourneyNodeInfo> journeyNodeInfos;
        JourneyNodeInfo journeyNodeInfo=new JourneyNodeInfo();
        journeyNodeInfo.setJourneyId(orderInfo.getJourneyId());
        journeyNodeInfos=journeyNodeInfoService.selectJourneyNodeInfoList(journeyNodeInfo);
        if(journeyNodeInfos.isEmpty()){
            throw new Exception("订单行程节点信息生成异常。");
        }
        for (JourneyNodeInfo jou:journeyNodeInfos) {
            jou.setPlanArriveTime(arrivalCalendarDate);
            int m=journeyNodeInfoService.updateJourneyNodeInfo(jou);
            if(m!=1){
                throw new Exception("更新订单行程节点信息异常。");
            }
        }


        ArrayList<JourneyPlanPriceInfo> arrayList=new ArrayList<>();
                                        arrayList.add(journeyPlanPriceInfoa);
        orderFullInfoBo.setJourneyPlanPriceInfos(arrayList);

        //更改调度详情到达时间
        OrderDispatcheDetailInfo orderDispatcheDetailInfo=new OrderDispatcheDetailInfo();
                                 orderDispatcheDetailInfo.setOrderId(orderInfo.getOrderId());
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos=orderDispatcheDetailInfoService.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo);
        if(!orderDispatcheDetailInfos.isEmpty()){
            if(orderDispatcheDetailInfos.size()>1){
                throw new Exception("订单调度信息生成异常。");
            }
            orderDispatcheDetailInfo=orderDispatcheDetailInfos.get(0);
            if(newUserTime<1.0){
                orderDispatcheDetailInfo.setCharterCarType("T001");
            }else if(newUserTime>1.0){
                orderDispatcheDetailInfo.setCharterCarType("T009");
            }else {
                orderDispatcheDetailInfo.setCharterCarType("T002");
            }
            int k=orderDispatcheDetailInfoService.updateOrderDispatcheDetailInfo(orderDispatcheDetailInfo);
            if(k!=1){
                throw new Exception("订单调度信息更新失败。");
            }
        }


        //更新订单地址到达时间
        OrderAddressInfo orderAddressInfo=new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderInfo.getOrderId());
        orderAddressInfo.setType("A999");
        List<OrderAddressInfo> orderAddressInfos=orderAddressInfoService.selectOrderAddressInfoList(orderAddressInfo);
        if(orderAddressInfos.size()!=1){
            throw new Exception("订单出发地址和到达地址信息生成异常。");
        }
        orderAddressInfo=orderAddressInfos.get(0);
        orderAddressInfo.setActionTime(arrivalCalendarDate);

        int l=orderAddressInfoService.updateOrderAddressInfo(orderAddressInfo);
        if(l!=1){
            throw new Exception("更新订单到达地址信息异常。");
        }


        return orderFullInfoBo;

    }

}
