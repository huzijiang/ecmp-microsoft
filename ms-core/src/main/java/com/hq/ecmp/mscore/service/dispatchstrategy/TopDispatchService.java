package com.hq.ecmp.mscore.service.dispatchstrategy;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;

    /**
     * 业务执行方法
     * @param dispatchSendCarDto
     */
    public void disBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        judgeIsFinish(dispatchSendCarDto);
        ((TopDispatchService)AopContext.currentProxy()).dispatchCommonBusiness(dispatchSendCarDto);
    }


    /**
     * 调度公共逻辑
     * @param dispatchSendCarDto
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void dispatchCommonBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
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
        if(dispatchSendCarDto.getCarId() != null){
            CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
            if(carInfo == null){
                throw new Exception("车辆id"+dispatchSendCarDto.getCarId()+",对应的车队不存在");
            }
            orderDispatcheDetailInfo.setCarCgId(carInfo.getCarGroupId());
        }
        orderDispatcheDetailInfo.setDriverId(dispatchSendCarDto.getDriverId());
        if(dispatchSendCarDto.getDriverId() !=null){
            CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
            carGroupDriverRelation.setDriverId(dispatchSendCarDto.getDriverId());
            List<CarGroupDriverRelation> carGroupDriverRelations = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
            if(CollectionUtils.isNotEmpty(carGroupDriverRelations)){
                Long carGroupId = carGroupDriverRelations.get(0).getCarGroupId();
                orderDispatcheDetailInfo.setDriverCgId(carGroupId);
            }else{
                throw new Exception("司机id"+dispatchSendCarDto.getDriverId()+",对应的车队不存在");
            }
        }
        if(dispatchSendCarDto.getIsFinishDispatch() == 1) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_YES_COMPLETE);
        }else if(dispatchSendCarDto.getIsFinishDispatch() == 2) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_NOT_COMPLETED);
        }
        orderDispatcheDetailInfo.setUpdateBy(String.valueOf(dispatchSendCarDto.getUserId()));
        orderDispatcheDetailInfo.setUpdateTime(DateUtils.getNowDate());
        if(dispatchSendCarDto.getInOrOut() == 1 ){
            orderDispatcheDetailInfo.setInnerDispatcher(dispatchSendCarDto.getUserId());
        }else if(dispatchSendCarDto.getInOrOut() == 2 ){
            orderDispatcheDetailInfo.setOuterDispatcher(dispatchSendCarDto.getUserId());
        }
        orderDispatcheDetailInfoMapper.updateOrderDispatcheDetailInfoByOrderId(orderDispatcheDetailInfo);
    }

    /**
     * 是否调度完成（是则状态为已完成调度，否则为未完成）
     * @param dispatchSendCarDto
     */
    public abstract  void judgeIsFinish(DispatchSendCarDto dispatchSendCarDto);
}
