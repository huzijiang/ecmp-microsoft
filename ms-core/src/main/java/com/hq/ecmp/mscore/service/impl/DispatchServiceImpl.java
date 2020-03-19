package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.mscore.bo.DispatcherOwnedContainer;
import com.hq.ecmp.mscore.domain.OrderAddressInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.dto.DispatchInfoDto;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyPassengerInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderAddressInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zj.hu
 * @Date: 2020-03-17 23:36
 */
@Service
public class DispatchServiceImpl implements IDispatchService {

    @Autowired
    OrderInfoMapper   orderInfoMapper;

    @Autowired
    OrderAddressInfoMapper orderAddressInfoMapper;

    @Autowired
    JourneyInfoMapper journeyInfoMapper;

    @Autowired
    JourneyPassengerInfoMapper journeyPassengerInfoMapper;



    /**
     *
     * 调度-获取可选择的车辆
     *
     * 注意 模糊查询 需要 取交集
     *
     * @param dispatchInfoDto
     * @return
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchInfoDto dispatchInfoDto) {
        Long orderId=Long.parseLong(dispatchInfoDto.getOrderNo());

        //队列检索
        String carKey=dispatchInfoDto.getDispatcherId().concat(dispatchInfoDto.getOrderNo());

        if(!DispatcherOwnedContainer.waitSelectedCars.containsValue(carKey)){
            OrderInfo orderInfo=orderInfoMapper.selectOrderInfoById(orderId);

            if(orderInfo==null){
                return ApiResponse.error("订单未找到");
            }
            OrderAddressInfo orderAddressInfoParam=new OrderAddressInfo();
                             orderAddressInfoParam.setOrderId(orderId);
                             orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            OrderAddressInfo orderAddressInfo=orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);
            //用车城市代码
            String userCarCity=orderAddressInfo.getCityPostalCode();
            //查询车辆级别
            String carModelLevelType=dispatchInfoDto.getCarModelLevelType();
            //乘客人数
            int passengeres=journeyPassengerInfoMapper.queryPeerCount(orderId)+1;


        }


        return null;
    }

    /**
     * 调度-锁定选择的车辆
     *
     * @param dispatchInfoDto
     * @return
     */
    @Override
    public ApiResponse lockSelectedCar(DispatchInfoDto dispatchInfoDto) {


        return null;
    }

    /**
     * 调度-获取可选择的司机
     *
     * @param dispatchInfoDto
     * @return
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchInfoDto dispatchInfoDto) {


        return null;
    }

    /**
     * 调度-锁定选择的司机
     *
     * @param dispatchInfoDto
     * @return
     */
    @Override
    public ApiResponse lockSelectedDriver(DispatchInfoDto dispatchInfoDto) {

        return null;
    }
}
