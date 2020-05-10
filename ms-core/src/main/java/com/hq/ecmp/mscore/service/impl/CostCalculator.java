package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.service.CostCalculation;

import java.math.BigDecimal;
import java.util.List;

/**
 * 成本计算器
 */
public class CostCalculator implements CostCalculation {

    /**
     * 成本计算
     * @param costConfigInfo
     * @return
     */
    @Override
    public OrderSettlingInfoVo calculator(CostConfigInfo costConfigInfo,OrderSettlingInfoVo orderSettlingInfoVo) {
        OrderSettlingInfoVo orderSettlingInfo = new OrderSettlingInfoVo();
        //服务类型
        String serviceType = costConfigInfo.getServiceType();
        //实际行程时间= 总时长 - 等待时长
        BigDecimal  waitingTime = new BigDecimal(orderSettlingInfoVo.getTotalTime()).subtract(orderSettlingInfoVo.getWaitingTime());
        //所有的费用
        BigDecimal amount= new BigDecimal("0");
        //订单里程的总价格
        BigDecimal totalPrice = new BigDecimal("0");
        //起步价
        BigDecimal startingPrice = new BigDecimal("0");
        //套餐价
        BigDecimal  packagePrice  = new BigDecimal("0");
        //超里程价格
        BigDecimal  overMileagePrice  = new BigDecimal("0");
        //超时长价格
        BigDecimal  overtimeLongPrice  = new BigDecimal("0");
        //等待费
        BigDecimal  waitingFee  = new BigDecimal("0");

        if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())){
            //包车的情况下
            //套餐价
            packagePrice= costConfigInfo.getCombosPrice();
            //超里程价格=（总里程-包含里程）* 超里程单价
            overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            //超时长价格=(总时长-包含时长)* 超时长单价
            overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            //总价=套餐价+（超里程价格）+（超时长价格)
            totalPrice =packagePrice.add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())){
            //预约
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            // 超里程价格=（总里程-包含里程）* 超里程单价
            overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            // 超时长价格=(实际行程时间-包含时长)* 超时长单价
            overtimeLongPrice=new BigDecimal(waitingTime.intValue()-(costConfigInfo.getCombosTimes().intValue())).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
            //接机
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            // 超里程价格=（总里程-包含里程）* 超里程单价
            overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            // 超时长价格=(实际行程时间-包含时长)* 超时长单价
            overtimeLongPrice=new BigDecimal(waitingTime.intValue()-(costConfigInfo.getCombosTimes().intValue())).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState())){
            //送机
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            // 超里程价格=（总里程-包含里程）* 超里程单价
            overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            // 超时长价格=(实际行程时间-包含时长)* 超时长单价
            overtimeLongPrice=new BigDecimal(waitingTime.intValue()-(costConfigInfo.getCombosTimes().intValue())).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState())){
            //即时用车
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            // 超里程价格=（总里程-包含里程）* 超里程单价
            overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            // 超时长价格=(实际行程时间-包含时长)* 超时长单价
            overtimeLongPrice=new BigDecimal(waitingTime.intValue()-(costConfigInfo.getCombosTimes().intValue())).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }
        //订单各项费用科目和对应费用
        amount=totalPrice
                .add(orderSettlingInfo.getRoadBridgeFee()
                .add(orderSettlingInfo.getHighSpeedFee())
                .add(orderSettlingInfo.getParkingRateFee()
                .add(orderSettlingInfo.getHotelExpenseFee())
                .add(orderSettlingInfo.getRestaurantFee())));
        //返回所需要的详情
        orderSettlingInfo.setStartingPrice(startingPrice);
        orderSettlingInfo.setOverMileagePrice(overMileagePrice);
        orderSettlingInfo.setOvertimeLongPrice(overtimeLongPrice);
        orderSettlingInfo.setWaitingFee(waitingFee);
        orderSettlingInfo.setAmount(amount);
        return orderSettlingInfo;
    }
}
