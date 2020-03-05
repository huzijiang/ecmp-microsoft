package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;

import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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

    /**
     * 初始化订单-创建订单
     *     根据行程申请信息生成订单信息
     *     改变订单的状态为  初始化
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "initOrder",notes = "初始化订单-创建订单",httpMethod ="POST")
    @PostMapping("/initOrder")
    public ApiResponse initOrder(JourneyApplyDto journeyApplyDto){
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
            if("A001".equals(applyType)){
                orderInfo.setState(OrderState.WAITINGLIST.getState());
            }else{
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交公务申请失败");
        }
        return ApiResponse.success("订单初始化成功");
    }



    /**
     * 用户请求调派订单
     * 改变订单的状态为  待派单
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "applyDispatchOrder",notes = "请求订单 调派车辆 ",httpMethod ="POST")
    @PostMapping("/applyDispatchOrder")
    public ApiResponse applyDispatchOrder(OrderDto orderDto){
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.WAITINGLIST.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if(i != 1){
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
     *   改变订单的状态为  去约车
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "letUserCallTaxi",notes = " 手动约车-让用户自己召唤网约车 改变订单的状态为  去约车",httpMethod ="POST")
    @PostMapping("/letUserCallTaxi")
    public ApiResponse letUserCallTaxi(OrderDto orderDto){
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.GETARIDE.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if(i != 1){
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
     *   改变订单的状态为  约车中
     *   需要留一个终止循环派单的 开关
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "letPlatCallTaxi",notes = "自动约车-向网约车平台发起约车请求 改变订单的状态为  约车中-->已派单",httpMethod ="POST")
    @PostMapping("/letPlatCallTaxi")
    public ApiResponse letPlatCallTaxi(OrderDto orderDto){
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderDto.getOrderId());
            orderInfo.setState(OrderState.SENDINGCARS.getState());
            int i = iOrderInfoService.updateOrderInfo(orderInfo);
            if(i != 1){
                return ApiResponse.error("'订单状态改为【约车中失败】");
            }
            do{
                //TODO 循环约车？？？ 待完善
                break;
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("'订单状态改为【去约车失败】");
        }
        return ApiResponse.success("订单状态修改成功");
    }


    /**
     * 调派订单  受到到订单的变化规则限制。此处为非网约车
     *      改变订单的状态为 已派单。
     *      如果是网约车  向网约车平台发起订单，等待返回后绑定
     *          如果是差旅，乘客需要自己向网约车发起订单，则该改变订单状态
     *      如果自有直接改变订单信息
     * @param  orderDto  行程申请信息
     * @param  carDto  行程申请信息
     * @param  driverDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "dispatchOrder",notes = "请求订单 调派车辆 ",httpMethod ="POST")
    @PostMapping("/dispatchOrder")
    public ApiResponse dispatchOrder(OrderDto orderDto, CarDto carDto, DriverDto driverDto){

        return null;
    }



    /**
     * 司机到达出发地，准备开始服务
     *    改变订单的状态为 准备服务
     * @param  orderDto  行程申请信息
     * @param  userDto   司机信息
     * @return
     */
    @ApiOperation(value = "readyServiceable",notes = "司机到达出发地，准备开始服务 ",httpMethod ="POST")
    @PostMapping("/readyServiceable")
    public ApiResponse readyServiceable(OrderDto orderDto,UserDto userDto){
        return null;
    }



    /**
     * 司机完成订单
     *    改变订单的状态为 服务结束
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "finishOrder",notes = "司机完成订单 ",httpMethod ="POST")
    @PostMapping("/finishOrder")
    public ApiResponse finishOrder(OrderDto orderDto,UserDto userDto){
        return null;
    }


    /**
     * 用户确认订单
     *    改变订单的状态为 订单关闭
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "affirmOrder",notes = "用户确认订单 ",httpMethod ="POST")
    @PostMapping("/affirmOrder")
    public ApiResponse affirmOrder(OrderDto orderDto,UserDto userDto){

        return null;
    }

    /**
     * 用户取消订单
     *    改变订单的状态为 订单关闭
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "cancelOrder",notes = "用户取消订单 ",httpMethod ="POST")
    @PostMapping("/cancelOrder")
    public ApiResponse cancelOrder(OrderDto orderDto,UserDto userDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            OrderInfo orderInfoOld = iOrderInfoService.selectOrderInfoById(orderDto.getOrderId());
            String useCarMode = orderInfoOld.getUseCarMode();
            String state = orderInfoOld.getState();
            if(useCarMode.equals(CarConstant.USR_CARD_MODE_NET)){
                //TODO 调用网约车的取消订单接口

            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            orderInfo.setCancelReason(orderDto.getCancelReason());
            orderInfo.setUpdateBy(String.valueOf(userId));
            iOrderInfoService.updateOrderInfo(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("订单取消失败");
        }
        return ApiResponse.success("订单取消成功");
    }


    /**
     * 获取所有等待 调度员 调派 的订单信息
     * @param  userDto  调度员用户信息
     * @return
     */
    @ApiOperation(value = "getAllWaitDispatchOrder",notes = "获取所有等待 调度员 调派 的订单信息 ",httpMethod ="POST")
    @PostMapping("/getAllWaitDispatchOrder")
    public ApiResponse<List<OrderInfo>> getAllWaitDispatchOrder(UserDto userDto){

        return null;
    }

    /**
     * 获取调派员已经完成调派的订单信息
     * @param  userDto  调度员用户信息
     * @return
     */
    @ApiOperation(value = "getUserDispatchedOrder",notes = "获取已经完成调派的订单信息 ",httpMethod ="POST")
    @PostMapping("/getUserDispatchedOrder")
    public ApiResponse<List<OrderInfo>> getUserDispatchedOrder(UserDto userDto){

        return null;
    }

    /**
     * 获取系统已经完成调派的订单信息
     * @return
     */
    @ApiOperation(value = "getAllDispatchedOrder",notes = "获取系统所有已经完成调派的订单信息 ",httpMethod ="POST")
    @PostMapping("/getAllDispatchedOrder")
    public ApiResponse<List<OrderInfo>> getAllDispatchedOrder(){

        return null;
    }

    /**
     * 获取等待调度的订单详细信息
     * @param  orderDto  订单信息
     * @return
     */
    @ApiOperation(value = "getWaitDispatchOrderDetailInfo",notes = "获取等待调度的订单详细信息",httpMethod ="POST")
    @PostMapping("/getWaitDispatchOrderDetailInfo")
    public ApiResponse<OrderInfo> getWaitDispatchOrderDetailInfo(OrderDto orderDto){

        return null;
    }


    /**
     * 评价 订单
     * @param  orderAppraiseDto   评价信息
     * @return
     */
    @ApiOperation(value = "appraiseOrder",notes = "评价 订单 ",httpMethod ="POST")
    @PostMapping("/appraiseOrder")
    public ApiResponse appraiseOrder(OrderAppraiseDto orderAppraiseDto){

        return null;
    }

    /**
    *   @author yj
    *   @Description  //TODO 获取乘客订单列表
    *   @Date 10:11 2020/3/4
    *   @Param  []
    *   @return com.hq.common.core.api.ApiResponse
    **/
        @ApiOperation(value = "我的行程订单列表",httpMethod = "POST")
    @RequestMapping("/getOrderList")
    public ApiResponse<List<OrderListInfo>> getIncompleteOrderList(@RequestBody PageRequest orderPage){
        List<OrderListInfo>  orderList;
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            orderList = iOrderInfoService.getOrderList(userId,orderPage.getPageNum(),orderPage.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("加载订单列表失败");
        }
        return ApiResponse.success(orderList);
    }
}
