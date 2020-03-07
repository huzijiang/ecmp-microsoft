package com.hq.ecmp.ms.api.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.ResignOrderTraceState;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDetailDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.ParallelOrderDto;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.util.MacTools;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;

    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;

    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;

    /**
     * 初始化订单-创建订单
     * 根据行程申请信息生成订单信息
     * 改变订单的状态为  初始化
     *
     * @param journeyApplyDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "initOrder", notes = "初始化订单-创建订单", httpMethod = "POST")
    @PostMapping("/initOrder")
    public ApiResponse initOrder(JourneyApplyDto journeyApplyDto) {
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            //申请表id
            Long applyId = journeyApplyDto.getApplyId();
            //行程id
            Long jouneyId = journeyApplyDto.getJouneyId();
            //获取行程节点信息
            JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
            journeyNodeInfo.setJourneyId(jouneyId);
            List<JourneyNodeInfo> journeyNodeInfos = iJourneyNodeInfoService.selectJourneyNodeInfoList(journeyNodeInfo);
            //获取行程主表信息
            JourneyInfo journeyInfo = iJourneyInfoService.selectJourneyInfoById(jouneyId);
            //获取申请表信息
            ApplyInfo applyInfo = iApplyInfoService.selectApplyInfoById(applyId);
            //插入订单初始信息
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setJourneyId(jouneyId);
            orderInfo.setDriverId(null);
            orderInfo.setCarId(null);
            //使用汽车的方式，自由和网约
            orderInfo.setUseCarMode(journeyInfo.getUseCarMode());
            orderInfo.setCreateBy(String.valueOf(userId));
            orderInfo.setCreateTime(new Date());
            String applyType = applyInfo.getApplyType();
            //如果是公务用车则状态直接为待派单，如果是差旅用车状态为初始化
            if ("A001".equals(applyType)) {
                orderInfo.setState(OrderState.WAITINGLIST.getState());
            } else {
                orderInfo.setState(OrderState.INITIALIZING.getState());
            }
            //有多少节点创建多少订单（注意往返以及差旅的室内用车）
            for (int i = 0; i < journeyNodeInfos.size(); i++) {
                //通过行程节点与申请id以及行程id唯一确定用户权限id
                Long nodeId = journeyNodeInfos.get(i).getNodeId();
                JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
                journeyUserCarPower.setApplyId(applyId);
                journeyUserCarPower.setNodeId(nodeId);
                journeyUserCarPower.setJourneyId(jouneyId);
                List<JourneyUserCarPower> journeyUserCarPowers = iJourneyUserCarPowerService.selectJourneyUserCarPowerList(journeyUserCarPower);
                Long powerId = journeyUserCarPowers.get(0).getPowerId();
                orderInfo.setNodeId(nodeId);
                orderInfo.setPowerId(powerId);// TODO: 2020/3/3  权限表何时去创建？ 申请审批通过以后创建用车权限表记录，一个行程节点对应一个用车权限，一个用车权限可能对应多个行程节点
                iOrderInfoService.insertOrderInfo(orderInfo);
                //插入订单轨迹表
                iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId));
                if ("A001".equals(applyType)) {
                    iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交公务申请失败");
        }
        return ApiResponse.success("订单初始化成功");
    }

    @ApiOperation(value = "parallelCreateOrder", notes = "手动创建订单，目前只存在市内用车的情况", httpMethod = "POST")
    @PostMapping("/parallelCreateOrder")
    public ApiResponse parallelCreateOrder(@RequestBody ParallelOrderDto parallelOrderDto) {
        Long orderId = null;
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setPowerId(parallelOrderDto.getTicketId());
            //通过用车权限获取行程id和行程节点id
            JourneyUserCarPower journeyUserCarPower = iJourneyUserCarPowerService.selectJourneyUserCarPowerById(parallelOrderDto.getTicketId());
            Long journeyId = journeyUserCarPower.getJourneyId();
            Long nodeId = journeyUserCarPower.getNodeId();
            orderInfo.setNodeId(nodeId);
            orderInfo.setJourneyId(journeyId);
            //手动下单，订单状态变为待派单
            orderInfo.setState(OrderState.WAITINGLIST.getState());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(parallelOrderDto.getBookingDate());
            orderInfo.setActualSetoutTime(date);
            orderInfo.setActualSetoutAddress(parallelOrderDto.getStartAddr());
            orderInfo.setActualArriveAddress(parallelOrderDto.getEndAddr());
            String startPoint = parallelOrderDto.getStartPoint();
            String endPoint = parallelOrderDto.getEndPoint();
            String[] start = startPoint.split("\\,");
            String[] end = endPoint.split("\\,");
            //出发地经纬度
            orderInfo.setActualSetoutLongitude(Long.parseLong(start[0]));
            orderInfo.setActualSetoutLatitude(Long.parseLong(start[1]));
            //目的地经纬度
            orderInfo.setActualArriveLongitude(Long.parseLong(end[0]));
            orderInfo.setActualArriveLatitude(Long.parseLong(end[1]));
            iOrderInfoService.insertOrderInfo(orderInfo);
            //订单轨迹
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId));
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId));
            orderId = orderInfo.getOrderId();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("手动下单失败");
        }
        JSONObject json = new JSONObject();
        json.put("orderId",orderId);
        return ApiResponse.success(json);
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
     * 手动约车-让用户自己召唤网约车
     * 改变订单的状态为  去约车
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "letUserCallTaxi", notes = " 手动约车-让用户自己召唤网约车 改变订单的状态为  去约车", httpMethod = "POST")
    @PostMapping("/letUserCallTaxi")
    public ApiResponse letUserCallTaxi(OrderDto orderDto) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.GETARIDE.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if (i != 1) {
                return ApiResponse.error("'订单状态改为【去约车失败】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("'订单状态改为【去约车失败】");
        }
        return ApiResponse.success("订单状态修改成功");
    }


    /**
     * 自动约车-向网约车平台发起约车请求，直到网约车平台回应 约到车为止，期间一直为约车中，约到后改变订单状态为 已派单
     * 改变订单的状态为  约车中
     * 需要留一个终止循环派单的 开关
     *
     * @param orderDto 行程申请信息
     * @return
     */
    @ApiOperation(value = "letPlatCallTaxi", notes = "自动约车-向网约车平台发起约车请求 改变订单的状态为  约车中-->已派单", httpMethod = "POST")
    @PostMapping("/letPlatCallTaxi")
    public ApiResponse letPlatCallTaxi(OrderDto orderDto) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.SENDINGCARS.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if (i != 1) {
                return ApiResponse.error("'订单状态改为【约车中失败】");
            }
            do {
                //TODO 循环约车？？？ 待完善
                break;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("'订单状态改为【去约车失败】");
        }
        return ApiResponse.success("订单状态修改成功");
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
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderDto.getOrderId()), OrderState.ORDERCLOSE.getState(), String.valueOf(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("行程确认失败");
        }
        return ApiResponse.success();
    }

    /**
     * 用户取消订单:
     * <p>
     * 逻辑：1.订单状态为未约到车的状态，直接更改订单状态为订单关闭。
     * 2. 订单状态为约到车未服务的状态 《1》：如果是网约车，调用网约车取消订单接口，取消订单，然后修改订单状态为订单关闭。
     * 《2》：如果是自有车，直接更改订单状态为订单关闭，成功以后给司机发送订单取消的消息通知。
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
            OrderInfo orderInfoOld = iOrderInfoService.selectOrderInfoById(orderDto.getOrderId());
            if (orderInfoOld == null) {
                throw new Exception("未查询到订单号【" + orderDto.getOrderId() + "】对应的订单信息");
            }
            String useCarMode = orderInfoOld.getUseCarMode();
            String state = orderInfoOld.getState();
            Long orderId = orderInfoOld.getOrderId();
            //状态为约到车未服务的状态，用车方式为网约车，调用三方取消订单接口
            if (OrderState.getContractedCar().contains(state) && useCarMode.equals(CarConstant.USR_CARD_MODE_NET)) {
                //TODO 调用网约车的取消订单接口
                List<String> macList = MacTools.getMacList();
                String macAdd = macList.get(0);
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("enterpriseId", enterpriseId);
                paramMap.put("enterpriseOrderId", String.valueOf(orderId));
                paramMap.put("licenseContent", licenseContent);
                paramMap.put("mac", macAdd);
                paramMap.put("reason", orderDto.getCancelReason());
                String result = OkHttpUtil.postJson(apiUrl + "/service/cancelOrder", paramMap);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!"0".equals(jsonObject.get("CODE"))) {
                    throw new Exception("调用三方取消订单服务-》取消失败");
                }
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            orderInfo.setCancelReason(orderDto.getCancelReason());
            orderInfo.setUpdateBy(String.valueOf(userId));
            int suc = iOrderInfoService.updateOrderInfo(orderInfo);
            //自有车，且状态变更成功
            if (suc == 1 && useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
                //TODO 调用消息通知接口，给司机发送乘客取消订单的消息
            }
            //插入订单轨迹表
            iOrderInfoService.insertOrderStateTrace(String.valueOf(orderDto.getOrderId()), OrderState.ORDERCLOSE.getState(), String.valueOf(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("订单取消失败->" + e.getMessage());
        }
        return ApiResponse.success("订单取消成功");
    }


    /**
     * 获取所有等待 调度员 调派 的订单信息(包含改派订单)
     * @param  userDto  调度员用户信息
     * @return
     */
    @ApiOperation(value = "getAllWaitDispatchOrder", notes = "获取所有等待 调度员 调派 的订单信息 ", httpMethod = "POST")
    @PostMapping("/getAllWaitDispatchOrder")
    public ApiResponse<List<DispatchOrderInfo>> getAllWaitDispatchOrder(UserDto userDto){
        return ApiResponse.success(iOrderInfoService.queryWaitDispatchList());
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
     * 获取系统已经完成调派的订单详细信息(包含待改派的)
     *
     * @return
     */
    @ApiOperation(value = "getCompleteDispatchOrderDetailInfo", notes = "获取系统所有已经完成调派的订单详细信息 ", httpMethod = "POST")
    @PostMapping("/getCompleteDispatchOrderDetailInfo")
    public ApiResponse<List<DispatchOrderInfo>> getCompleteDispatchOrderDetailInfo() {
         
        return null;
    }

    /**
     * 获取等待调度的订单详细信息(包含待改派的)
     *
     * @param orderDto 订单信息
     * @return
     */
    @ApiOperation(value = "getWaitDispatchOrderDetailInfo", notes = "获取等待调度的订单详细信息", httpMethod = "POST")
    @PostMapping("/getWaitDispatchOrderDetailInfo")
    public ApiResponse<DispatchOrderInfo> getWaitDispatchOrderDetailInfo(Long orderId) {
        return ApiResponse.success(iOrderInfoService.getWaitDispatchOrderDetailInfo(orderId));
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
            return ApiResponse.error("获取司机的任务列表失败");
        }
        return ApiResponse.success(driverOrderList);
    }
}
