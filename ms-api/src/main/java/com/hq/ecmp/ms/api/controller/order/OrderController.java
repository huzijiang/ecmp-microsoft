package com.hq.ecmp.ms.api.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDetailDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.DriverOrderInfoVO;
import com.hq.ecmp.mscore.vo.OfficialOrderReVo;
import com.hq.ecmp.mscore.vo.OrderStateVO;
import com.hq.ecmp.mscore.vo.OrderVO;
import com.hq.ecmp.util.MacTools;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IOrderInfoService iOrderInfoService;

    @Resource
    private IJourneyNodeInfoService iJourneyNodeInfoService;

    @Resource
    private IJourneyInfoService iJourneyInfoService;

    @Resource
    private IApplyInfoService iApplyInfoService;

    @Resource
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;

    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Resource
    private DriverServiceAppraiseeInfoService driverServiceAppraiseeInfoService;

    @Autowired
    private IDriverHeartbeatInfoService driverHeartbeatInfoService;

    @Resource
    private EcmpMessageService ecmpMessageService;

    @Resource
    private IOrderAddressInfoService iOrderAddressInfoService;


    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;

    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;

    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;
    @Value("${order.shareUrl}")//行程分享的详情路径
    private String shareUrl;

    /**
     * 初始化订单-公务创建订单
     * 根据行程申请信息生成订单信息
     * 改变订单的状态为  初始化
     *
     * @param OfficialOrderReVo
     * @return
     */
    @ApiOperation(value = "公务创建订单", notes = "公务创建订单", httpMethod = "POST")
    @PostMapping("/officialOrder")
    public ApiResponse officialOrder(@RequestBody OfficialOrderReVo OfficialOrderReVo) {
        Long orderId;
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            orderId = iOrderInfoService.officialOrder(OfficialOrderReVo,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("公务下单成功",orderId);
    }

//    @ApiOperation(value = "parallelCreateOrder", notes = "手动创建订单，目前只存在市内用车的情况", httpMethod = "POST")
//    @PostMapping("/parallelCreateOrder")
//    public ApiResponse parallelCreateOrder(@RequestBody ParallelOrderDto parallelOrderDto) {
//        Long orderId = null;
//        try {
//
//            //获取调用接口的用户信息
//            HttpServletRequest request = ServletUtils.getRequest();
//            LoginUser loginUser = tokenService.getLoginUser(request);
//            Long userId = loginUser.getUser().getUserId();
//            OrderInfo orderInfo = new OrderInfo();
//            orderInfo.setPowerId(parallelOrderDto.getTicketId());
//            //通过用车权限获取行程id和行程节点id
//            JourneyUserCarPower journeyUserCarPower = iJourneyUserCarPowerService.selectJourneyUserCarPowerById(parallelOrderDto.getTicketId());
//            Long journeyId = journeyUserCarPower.getJourneyId();
//            Long nodeId = journeyUserCarPower.getNodeId();
//            orderInfo.setNodeId(nodeId);
//            orderInfo.setJourneyId(journeyId);
//            //手动下单，订单状态变为待派单
//            orderInfo.setState(OrderState.WAITINGLIST.getState());
//            String startPoint = parallelOrderDto.getStartPoint();
//            String endPoint = parallelOrderDto.getEndPoint();
//            String[] start = startPoint.split("\\,");
//            String[] end = endPoint.split("\\,");
//            iOrderInfoService.insertOrderInfo(orderInfo);
//            orderId = orderInfo.getOrderId();
//            //订单地址表
//            JourneyInfo journeyInfo = iJourneyInfoService.selectJourneyInfoById(journeyId);
//            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
//            orderAddressInfo.setOrderId(orderId);
//            orderAddressInfo.setJourneyId(journeyId);
//            orderAddressInfo.setNodeId(nodeId);
//            orderAddressInfo.setPowerId(parallelOrderDto.getTicketId());
//            orderAddressInfo.setUserId(journeyInfo.getUserId()+"");
//            orderAddressInfo.setCreateBy(userId+"");
//            //起点
//            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = sdf.parse(parallelOrderDto.getBookingDate());
//            orderAddressInfo.setActionTime(date);
//            orderAddressInfo.setLongitude(Double.parseDouble(start[0]));
//            orderAddressInfo.setLatitude(Double.parseDouble(start[1]));
//            orderAddressInfo.setAddress(parallelOrderDto.getStartAddr());
//            orderAddressInfo.setAddressLong(parallelOrderDto.getStarAddrLong());
//            iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
//            //终点
//            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
//            orderAddressInfo.setActionTime(null);
//            orderAddressInfo.setLongitude(Double.parseDouble(end[0]));
//            orderAddressInfo.setLatitude(Double.parseDouble(end[1]));
//            orderAddressInfo.setAddress(parallelOrderDto.getEndAddr());
//            orderAddressInfo.setAddressLong(parallelOrderDto.getEndAddrLong());
//            iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
//            //订单轨迹
//            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId),null);
//            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId),null);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiResponse.error("手动下单失败");
//        }
//        JSONObject json = new JSONObject();
//        json.put("orderId",orderId);
//        return ApiResponse.success(json);
//    }


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


//    /**
//     * 手动约车-让用户自己召唤网约车
//     * 改变订单的状态为  去约车
//     *
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "letUserCallTaxi", notes = " 手动约车-让用户自己召唤网约车 改变订单的状态为  去约车", httpMethod = "POST")
//    @PostMapping("/letUserCallTaxi")
//    public ApiResponse letUserCallTaxi(@RequestParam("orderNo") String orderNo,@RequestParam("carLevel") String carLevel) {
//        try {
//            //获取调用接口的用户信息
//            HttpServletRequest request = ServletUtils.getRequest();
//            LoginUser loginUser = tokenService.getLoginUser(request);
//            Long userId = loginUser.getUser().getUserId();
//            OrderInfo orderInfo = new OrderInfo();
//            Long orderId = Long.parseLong(orderNo);
//            orderInfo.setOrderId(orderId);
//            orderInfo.setState(OrderState.SENDINGCARS.getState());
//            int i = iOrderInfoService.updateOrderInfo(orderInfo);
//            if (i != 1) {
//                return ApiResponse.error("'订单状态改为【去约车失败】");
//            }
//            iOrderInfoService.platCallTaxi(orderId,enterpriseId,licenseContent,apiUrl,String.valueOf(userId),carLevel);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiResponse.error("'订单状态改为【去约车失败】");
//        }
//        return ApiResponse.success("订单状态修改成功");
//    }


    /**
     * 自动约车-向网约车平台发起约车请求，直到网约车平台回应 约到车为止，期间一直为约车中，约到后改变订单状态为 已派单
     * 改变订单的状态为  约车中
     * 需要留一个终止循环派单的 开关
     *
     * @param
     * @return
     */
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
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setState(OrderState.SENDINGCARS.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if (i != 1) {
                throw new Exception("约车失败");
            }
            iOrderInfoService.platCallTaxi(orderId,enterpriseId,licenseContent,apiUrl,String.valueOf(userId),carLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success(e.getMessage());
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
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderDto.getOrderId()), OrderState.ORDERCLOSE.getState(), String.valueOf(userId),null);
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
     * 获取所有等待 调度员 调派 的订单信息(包含改派订单)
     * 
     * 自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的驾驶员能看到该订单
     * 
     * 只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单
     * @param  userDto  调度员用户信息
     * @return
     */
    @ApiOperation(value = "getAllWaitDispatchOrder", notes = "获取所有等待 调度员 调派 的订单信息 ", httpMethod = "POST")
    @PostMapping("/getAllWaitDispatchOrder")
    public ApiResponse<List<DispatchOrderInfo>> getAllWaitDispatchOrder(UserDto userDto){
    	HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        return ApiResponse.success(iOrderInfoService.queryWaitDispatchList(userId));
    }

    /**
     * 获取调派员已经完成调派的订单信息
     *
     * @param userDto 调度员用户信息
     * @return
     */
    @ApiOperation(value = "getUserDispatchedOrder", notes = "获取已经完成调派的订单信息 ", httpMethod = "POST")
    @PostMapping("/getUserDispatchedOrder")
    public ApiResponse<List<DispatchOrderInfo>> getUserDispatchedOrder(UserDto userDto){
        return ApiResponse.success(iOrderInfoService.queryCompleteDispatchOrder());
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
        return ApiResponse.success(iOrderInfoService.getWaitDispatchOrderDetailInfo(orderId));
    }


    /**
     * 自有车派车
     * @param orderId
     * @param driverId
     * @param carId
     * @return
     */
    @ApiOperation(value = "ownCarSendCar", notes = "自有车派车", httpMethod = "POST")
    @PostMapping("/ownCarSendCar")
    public ApiResponse ownCarSendCar(Long orderId,Long driverId,Long carId) {
    	 HttpServletRequest request = ServletUtils.getRequest();
         LoginUser loginUser = tokenService.getLoginUser(request);
         Long userId = loginUser.getUser().getUserId();
         boolean ownCarSendCar = iOrderInfoService.ownCarSendCar(orderId, driverId, carId, userId);
         if(ownCarSendCar){
        	 return ApiResponse.success();
         }else{
        	 return ApiResponse.error("调派单【"+orderId+"】自有车派车失败");
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

    @ApiOperation(value = "查询订单详情列表", httpMethod = "POST")
    @RequestMapping("/orderDetail")
    public ApiResponse<OrderDetailDto> orderDetail(@RequestHeader("token") String token, @RequestParam("orderNo") String orderId) {

        return ApiResponse.success();
    }

    @ApiOperation(value = "改派订单", httpMethod = "POST")
    @RequestMapping("/reassign")
    public ApiResponse reassign(@RequestParam(value = "orderNo") String orderNo,
                                @RequestParam(value = "rejectReason") String rejectReason,
                                @RequestParam(value = "status") String status) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            if ("1".equals(status)) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setState(OrderState.WAITINGLIST.getState());
                orderInfo.setUpdateBy(String.valueOf(userId));
                orderInfo.setOrderId(Long.parseLong(orderNo));
                iOrderInfoService.updateOrderInfo(orderInfo);
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                orderStateTraceInfo.setCreateBy(String.valueOf(userId));
                orderStateTraceInfo.setState(ResignOrderTraceState.AGREE.getState());
                orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
                iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
            } else if ("2".equals(status)) {
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                orderStateTraceInfo.setCreateBy(String.valueOf(userId));
                orderStateTraceInfo.setState(ResignOrderTraceState.DISAGREE.getState());
                orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
                orderStateTraceInfo.setContent(rejectReason);
                iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
            } else {
                throw new Exception("操作异常");
            }
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
    public ApiResponse<List<OrderDriverListInfo>> driverOrderList(@RequestBody PageRequest driverListRequest) {
        List<OrderDriverListInfo> driverOrderList = null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            driverOrderList = iOrderInfoService.getDriverOrderList(userId, driverListRequest.getPageNum(), driverListRequest.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success(driverOrderList);
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
//            Long userId = loginUser.getDriver().getDriverId();
            Long userId=loginUser.getUser().getUserId();
            driverOrderList = iOrderInfoService.driverOrderUndoneList(userId, driverListRequest.getPageNum(), driverListRequest.getPageSize(),driverListRequest.getDay());
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
            Long userId = loginUser.getUser().getUserId();
            int count = iOrderInfoService.driverOrderCount(userId);
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
            driverServiceAppraiseeInfo.setScore(orderDriverAppraiseDto.getScore());
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
    public ApiResponse<OrderVO> orderBeServiceDetail(@RequestBody OrderDto orderDto) {
        try {
            OrderVO orderVO = iOrderInfoService.orderBeServiceDetail(orderDto.getOrderId());
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
    @Transactional
    public ApiResponse<OrderStateVO> getOrderState(@RequestBody OrderDto orderDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            OrderStateVO  orderVO = iOrderInfoService.getOrderState(orderDto.getOrderId());
            orderVO.setDriverLongitude("116.786324");
            orderVO.setDriverLatitude("39.563521");
            //TODO 记得生产放开
//            if (CarConstant.USR_CARD_MODE_HAVE.equals(orderVO.getUseCarMode())){//自有车
//                DriverHeartbeatInfo driverHeartbeatInfo = driverHeartbeatInfoService.findNowLocation(orderVO.getDriverId(), orderDto.getOrderId());
//                String latitude=driverHeartbeatInfo.getLatitude().stripTrailingZeros().toPlainString();
//                String longitude=driverHeartbeatInfo.getLongitude().stripTrailingZeros().toPlainString();
//                orderVO.setDriverLongitude(longitude);
//                orderVO.setDriverLatitude(latitude);
//                //获取出发地目的地
//                if (orderVO.getApplyType().equals(ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey())){//公务
//                    //获取坐标和地址
//                    List<JourneyNodeInfo> journeyNodeInfos = iJourneyNodeInfoService.selectJourneyNodeInfoList(new JourneyNodeInfo(orderVO.getJourneyId(), CommonConstant.NO_PASS));
//                    if (CollectionUtils.isNotEmpty(journeyNodeInfos)&&journeyNodeInfos.size()==2){
//                        JourneyNodeInfo endNode = journeyNodeInfos.get(0);//结束点
//                        JourneyNodeInfo startNode = journeyNodeInfos.get(1);//结束点
//                        orderVO.setEndAddress(endNode.getPlanEndAddress());
//                        orderVO.setEndLatitude(endNode.getPlanEndLatitude());
//                        orderVO.setEndLongitude(endNode.getPlanBeginLongitude());
//                        orderVO.setStartAddress(startNode.getPlanBeginAddress());
//                        orderVO.setStartLongitude(startNode.getPlanBeginLongAddress());
//                        orderVO.setStartLatitude(startNode.getPlanBeginLatitude());
//                    }
//                }
//                return ApiResponse.success(orderVO);
//            }else {
//                JSONObject taxiOrderState = iOrderInfoService.getTaxiOrderState(orderDto.getOrderId(), enterpriseId, licenseContent, MacTools.getMacList().get(0), apiUrl);
//                String longitude="";
//                String latitude="";
//                if(taxiOrderState!=null){
//                  JSONObject data1 = taxiOrderState.getJSONObject("data");
//                        longitude=data1.getString("x");
//                        latitude=data1.getString("y");
//                }
//                OrderStateVO  orderVO1 = iOrderInfoService.getOrderState(orderDto.getOrderId());
//                orderVO1.setDriverLongitude(longitude);
//                orderVO1.setDriverLatitude(latitude);
                return ApiResponse.success(orderVO);
//            }
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("获取订单状态失败");
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
        String url=shareUrl;
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

    @ApiOperation(value = "自有车手动约车接口", notes = "自有车手动约车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/letUserCallTaxiPrivate", method = RequestMethod.POST)
    public ApiResponse letUserCallTaxiPrivate( @RequestParam("orderNo") String orderNo){
        try {
            Long orderId = Long.parseLong(orderNo);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setState(OrderState.WAITINGLIST.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if (i != 1) {
                throw new Exception("约车失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success();
    }
}
