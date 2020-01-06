package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.order.OrderAppraiseDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@RequestMapping("/order")
public class OrderController {

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

        return null;
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

        return null;
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

        return null;
    }


    /**
     * 自动约车-向网约车平台发起约车请求，知道网约车平台回应 约到车为止，期间一直为约车中，约到后改变订单状态为 已派单
     *   改变订单的状态为  约车中
     *   需要留一个终止循环派单的 开关
     * @param  orderDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "letPlatCallTaxi",notes = "自动约车-向网约车平台发起约车请求 改变订单的状态为  约车中-->已派单",httpMethod ="POST")
    @PostMapping("/letPlatCallTaxi")
    public ApiResponse letPlatCallTaxi(OrderDto orderDto){

        return null;
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

        return null;
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




}
