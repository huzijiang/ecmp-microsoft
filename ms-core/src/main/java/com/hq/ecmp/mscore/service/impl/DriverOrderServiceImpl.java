package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.DriverBehavior;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.OrderWaitTraceInfo;
import com.hq.ecmp.mscore.service.IDriverOrderService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import com.hq.ecmp.mscore.service.IOrderWaitTraceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DriverOrderServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/10 14:36
 * @Version 1.0
 */
@Service
public class DriverOrderServiceImpl implements IDriverOrderService {

    @Resource
    IOrderInfoService iOrderInfoService;

    @Resource
    IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Resource
    IOrderWaitTraceInfoService iOrderWaitTraceInfoService;


    @Override
    @Transactional(rollbackFor = Exception.class)
        public void handleDriverOrderStatus(String type, String currentPoint, String orderNo,Long userId) throws Exception {
        String[] point = currentPoint.split("\\,| \\，");
        Long longitude = Long.parseLong(point[0]);
        Long latitude = Long.parseLong(point[1]);
        long orderId = Long.parseLong(orderNo);
        //订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateBy(String.valueOf(userId));
        //订单轨迹
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setDriverLongitude(longitude);
        orderStateTraceInfo.setDriverLatitude(latitude);
        orderStateTraceInfo.setCreateBy(String.valueOf(userId));
        if(DriverBehavior.PICKUP_PASSENGER.getType().equals(type)){
            //订单状态
            orderInfo.setState(OrderState.ALREADY_SET_OUT.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.ALREADY_SET_OUT.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);

        }else if(DriverBehavior.ARRIVE.getType().equals(type)){
            //订单状态
            orderInfo.setState(OrderState.READYSERVICE.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.PRESERVICE.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else if((DriverBehavior.START_SERVICE.getType().equals(type))){
            //订单状态
            orderInfo.setState(OrderState.INSERVICE.getState());
            orderInfo.setActualSetoutLongitude(longitude);
            orderInfo.setActualSetoutLatitude(latitude);
            orderInfo.setActualSetoutTime(DateUtils.getNowDate());
            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表（先空下）
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.SERVICE.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else if((DriverBehavior.SERVICE_COMPLETION.getType().equals(type))){
            orderInfo.setState(OrderState.STOPSERVICE.getState());
            orderInfo.setActualArriveLongitude(longitude);
            orderInfo.setActualArriveLatitude(latitude);
            orderInfo.setActualArriveTime(DateUtils.getNowDate());
            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表（先空下）
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.SERVICEOVER.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else{
            throw new Exception("操作类型有误");
        }
    }

    @Override
    public void isContinue() {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void waitingOrder(String orderNo, String isFinish, String currentPoint,String userId) {
        Long orderId = Long.parseLong(orderNo);
        String[] point = currentPoint.split("\\,| \\，");
        Long longitude = Long.parseLong(point[0]);
        Long latitude = Long.parseLong(point[1]);
        if(CommonConstant.START.equals(isFinish)){
            OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(orderId);
            OrderWaitTraceInfo orderWaitTraceInfo = new OrderWaitTraceInfo();
            orderWaitTraceInfo.setOrderId(orderId);
            orderWaitTraceInfo.setJourneyId(orderInfo.getJourneyId());
            orderWaitTraceInfo.setDirverId(orderInfo.getDriverId());
            orderWaitTraceInfo.setCarLicense(orderInfo.getCarLicense());
            orderWaitTraceInfo.setStartTime(DateUtils.getNowDate());
            orderWaitTraceInfo.setLongitude(longitude);
            orderWaitTraceInfo.setLatitude(latitude);
            orderWaitTraceInfo.setCreateBy(userId);
            iOrderWaitTraceInfoService.insertOrderWaitTraceInfo(orderWaitTraceInfo);
        }else if(CommonConstant.FINISH.equals(isFinish)){
            OrderWaitTraceInfo orderWaitTraceInfoQuery = new OrderWaitTraceInfo();
            orderWaitTraceInfoQuery.setOrderId(orderId);
            List<OrderWaitTraceInfo> orderWaitTraceInfos = iOrderWaitTraceInfoService.selectOrderWaitTraceInfoList(orderWaitTraceInfoQuery);
            OrderWaitTraceInfo orderWaitTraceInfo = orderWaitTraceInfos.get(0);
            Date startTime = orderWaitTraceInfo.getStartTime();
            Long duration = (DateUtils.getNowDate().getTime()-startTime.getTime())/1000;
            OrderWaitTraceInfo orderWaitTraceInfoParam = new OrderWaitTraceInfo();
            orderWaitTraceInfoParam.setEndTime(DateUtils.getNowDate());
            orderWaitTraceInfoParam.setDuration(duration);
            orderWaitTraceInfoParam.setUpdateBy(userId);
            iOrderWaitTraceInfoService.updateOrderWaitTraceInfo(orderWaitTraceInfoParam);
        }
    }
}
