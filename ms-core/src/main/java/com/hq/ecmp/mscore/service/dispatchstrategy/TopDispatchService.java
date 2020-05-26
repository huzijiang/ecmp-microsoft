package com.hq.ecmp.mscore.service.dispatchstrategy;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.OrderDispatcheDetailInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/26 10:51
 * @Version 1.0
 */
@Service
public abstract class TopDispatchService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;
    @Resource
    private  OrderStateTraceInfoMapper orderStateTraceInfoMapper;

    /**
     * 业务执行方法
     * @param dispatchSendCarDto
     */
    public void disBusiness(DispatchSendCarDto dispatchSendCarDto){
        judgeIsFinish(dispatchSendCarDto);
        dispatchCommonBusiness(dispatchSendCarDto);
    }


    /**
     * 调度公共逻辑
     * @param dispatchSendCarDto
     */
    private void dispatchCommonBusiness(DispatchSendCarDto dispatchSendCarDto){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(dispatchSendCarDto.getOrderId());
        orderInfo.setCarId(dispatchSendCarDto.getCarId());
        orderInfo.setDriverId(dispatchSendCarDto.getDriverId());
        orderInfo.setUpdateBy(String.valueOf(dispatchSendCarDto.getUserId()));
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        if(dispatchSendCarDto.getIsFinishDispatch() == 1){
            orderInfo.setState(OrderState.ALREADYSENDING.getState());
        }
        orderInfoMapper.updateOrderInfo(orderInfo);

        if(dispatchSendCarDto.getIsFinishDispatch() == 1) {
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setOrderId(dispatchSendCarDto.getOrderId());
            orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
            orderStateTraceInfo.setCreateBy(String.valueOf(dispatchSendCarDto.getUserId()));
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        }
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
        orderDispatcheDetailInfo.setOrderId(dispatchSendCarDto.getOrderId());
        orderDispatcheDetailInfo.setItIsUseInnerCarGroup(dispatchSendCarDto.getUseCarGroupType());
        orderDispatcheDetailInfo.setCarGroupUserMode(dispatchSendCarDto.getCarGroupUseMode());
        orderDispatcheDetailInfo.setItIsSelfDriver(dispatchSendCarDto.getSelfDrive());
        orderDispatcheDetailInfo.setNextCarGroupId(dispatchSendCarDto.getOutCarGroupId());
        orderDispatcheDetailInfo.setCharterCarType(dispatchSendCarDto.getCharterType());
        orderDispatcheDetailInfo.setCarId(dispatchSendCarDto.getCarId());
        orderDispatcheDetailInfo.setCarCgId(dispatchSendCarDto.getCarGroupId());
        orderDispatcheDetailInfo.setDriverId(dispatchSendCarDto.getDriverId());

        orderDispatcheDetailInfo.setDriverCgId(dispatchSendCarDto.getDriverCarGroupId());
        if(dispatchSendCarDto.getIsFinishDispatch() == 1) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_YES_COMPLETE);
        }else if(dispatchSendCarDto.getIsFinishDispatch() == 2) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_NOT_COMPLETED);
        }
        orderDispatcheDetailInfo.setUpdateBy(String.valueOf(dispatchSendCarDto.getUserId()));
        orderDispatcheDetailInfo.setUpdateTime(DateUtils.getNowDate());

        orderDispatcheDetailInfoMapper.updateOrderDispatcheDetailInfoByOrderId(orderDispatcheDetailInfo);
    }

    /**
     * 是否最后已调度（是则状态为已完成调度，否则为未完成）
     * @param dispatchSendCarDto
     */
    public abstract  void judgeIsFinish(DispatchSendCarDto dispatchSendCarDto);
}
