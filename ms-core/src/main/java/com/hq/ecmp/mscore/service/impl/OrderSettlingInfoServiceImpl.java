package com.hq.ecmp.mscore.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.CostCalculation;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderSettlingInfoServiceImpl implements IOrderSettlingInfoService
{
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Autowired
    private OrderAddressInfoMapper OrderAddressInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CostConfigInfoMapper costConfigInfoMapper;
    @Autowired
    private OrderWaitTraceInfoMapper orderWaitTraceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param billId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderSettlingInfo selectOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoById(billId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderSettlingInfo> selectOrderSettlingInfoList(OrderSettlingInfo orderSettlingInfo)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoList(orderSettlingInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setCreateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setUpdateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.updateOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param billIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoByIds(Long[] billIds)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoByIds(billIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param billId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoById(billId);
    }

    /**
     * 司机端费用上报提交
     * @param orderSettlingInfoVo
     * @param userId
     */
    @Override
    public int addExpenseReport(OrderSettlingInfoVo orderSettlingInfoVo, Long userId,String companyId) {
        //计算等待时长
        BigDecimal waitingTime = orderWaitTraceInfoMapper.selectOrderWaitingTimeById(orderSettlingInfoVo.getOrderId());
        orderSettlingInfoVo.setWaitingTime(waitingTime);
        //查询该订单的记录  城市  服务类型  车型级别  包车类型 公司id
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderSettlingInfoVo.getOrderId());
        //查询所在的城市
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderSettlingInfoVo.getOrderId());
        OrderAddressInfo addressInfo = OrderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfo);
        //城市
        String cityCode = addressInfo.getCityPostalCode();
        //车型级别
        CarInfo carInfo = carInfoMapper.selectCarInfoById(orderInfo.getCarId());
        Long carLevel = carInfo.getCarTypeId();
        //筛选出成本数据model
        CostConfigQueryDto costConfigQueryDto = new CostConfigQueryDto();
        //公司id
        costConfigQueryDto.setCompanyId(companyId);
        //城市
        costConfigQueryDto.setCityCode(Integer.valueOf(cityCode));
        //服务类型
        costConfigQueryDto.setServiceType(orderInfo.getServiceType());
        //车型级别
        costConfigQueryDto.setCarTypeId(carLevel);
        CostConfigInfo costConfigInfo =null;
        //服务类型为包车
        if (orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())) {
            //服务类型属于包车
            //包车类型:半日租，整日租
            JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
            //T000  非包车 T001 半日租（4小时） T002 整日租（8小时）
            String carType = journeyInfo.getCharterCarType();
            //包车类型
            costConfigQueryDto.setRentType(carType);
            List<CostConfigListResult> costConfigListResult = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
            costConfigQueryDto.setCostId(costConfigListResult.get(0).getCostId());
            costConfigInfo = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
        } else {
            List<CostConfigListResult> costConfigListResult = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
            costConfigQueryDto.setCostId(costConfigListResult.get(0).getCostId());
            costConfigInfo = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
        }
        //计算成本的方法
        CostCalculation calculator = new CostCalculator();
        OrderSettlingInfoVo orderSettlingInfo = calculator.calculator(costConfigInfo, orderSettlingInfoVo);
        OrderSettling OrderSettling =new OrderSettling();
        //默认个人取消费用为0
        BigDecimal personalCancellationFee = new BigDecimal("0");
        //默认个人取消费用为0
        BigDecimal enterpriseCancellationFee = new BigDecimal("0");
        //路桥费
        OrderSettling.setRoadBridgeFee(orderSettlingInfoVo.getRoadBridgeFee());
        //高速费
        OrderSettling.setHighSpeedFee(orderSettlingInfoVo.getHighSpeedFee());
        //停车费
        OrderSettling.setParkingRateFee(orderSettlingInfoVo.getParkingRateFee());
        //住宿费
        OrderSettling.setHotelExpenseFee(orderSettlingInfoVo.getHotelExpenseFee());
        //餐饮费
        OrderSettling.setRestaurantFee(orderSettlingInfoVo.getRestaurantFee());
        //超里程价格
        OrderSettling.setOverMileagePrice(orderSettlingInfo.getOverMileagePrice());
        //超时长价格
        OrderSettling.setOvertimeLongPrice(orderSettlingInfo.getOvertimeLongPrice());
        //起步价
        OrderSettling.setStartingPrice(orderSettlingInfo.getStartingPrice());
        //等待费
        OrderSettling.setWaitingFee(orderSettlingInfo.getWaitingFee());
        //个人取消费
        OrderSettling.setPersonalCancellationFee(personalCancellationFee);
        //企业取消费
        OrderSettling.setEnterpriseCancellationFee(enterpriseCancellationFee);
        String json= JSON.toJSONString(OrderSettling);
        //落库到订单结算信息表
        orderSettlingInfoVo.setCreateTime(DateUtils.getNowDate());
        orderSettlingInfoVo.setCreateBy(userId.toString());
        orderSettlingInfoVo.setAmountDetail(json);
        orderSettlingInfoVo.setAmount(orderSettlingInfo.getAmount());
        int i = orderSettlingInfoMapper.insertOrderSettlingInfoOne(orderSettlingInfoVo);
        return i;
    }
}
