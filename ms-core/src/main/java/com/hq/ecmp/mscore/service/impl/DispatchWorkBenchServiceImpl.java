package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.ecmp.constant.enumerate.DispatchListState;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.DispatchWorkBenchService;
import com.hq.ecmp.mscore.vo.DisOrderStateCount;
import com.hq.ecmp.mscore.vo.DisWorkBenchCar;
import com.hq.ecmp.mscore.vo.DisWorkBenchInfo;
import com.hq.ecmp.mscore.vo.DisWorkBenchOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 15:37
 */
@Service
public class DispatchWorkBenchServiceImpl implements DispatchWorkBenchService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private CarInfoMapper carInfoMapper;

    @Override
    public DisWorkBenchInfo getDispatchInfoListWorkBench(String state,Integer pageNum,Integer pageSize) {
        List<DisOrderStateCount> stateCount = new ArrayList<>();
        List<DisOrderStateCount> dispatchOrderListWorkBenchCount = orderInfoMapper.getDispatchOrderListWorkBenchCount(null);
        List<DisOrderStateCount> dispatchOrderListWorkBenchCount1 = orderInfoMapper.getDispatchOrderListWorkBenchCount(DispatchListState.alreadySendCar.getStateName());
        List<DisOrderStateCount> carByStateCount = carInfoMapper.getCarByStateCount();
        stateCount.addAll(dispatchOrderListWorkBenchCount);
        stateCount.addAll(dispatchOrderListWorkBenchCount1);
        stateCount.addAll(carByStateCount);
        Long total = 0L;
        DisWorkBenchInfo disWorkBenchInfo = new DisWorkBenchInfo();
        if (DispatchListState.getOrderState().contains(state)){
            PageHelper.startPage(pageNum, pageSize);
            List<DisWorkBenchOrder> dispatchOrderListWorkBench = orderInfoMapper.getDispatchOrderListWorkBench(state);
            PageInfo<DisWorkBenchOrder> pageInfo = new PageInfo<>(dispatchOrderListWorkBench);
            total = pageInfo.getTotal();
            disWorkBenchInfo.setDisWorkbenchOrders(dispatchOrderListWorkBench);
        }else if (DispatchListState.getCarState().contains(state)){
            PageHelper.startPage(pageNum, pageSize);
            List<DisWorkBenchCar> carByState = carInfoMapper.getCarByState(state);
            disWorkBenchInfo.setDisWorkBenchCars(carByState);
            PageInfo<DisWorkBenchCar> pageInfo = new PageInfo<>(carByState);
            total = pageInfo.getTotal();
        }
        disWorkBenchInfo.setTotal(total);
        disWorkBenchInfo.setStateCount(stateCount);
        return disWorkBenchInfo;
    }
}
