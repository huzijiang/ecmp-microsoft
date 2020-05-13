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
        BigDecimal amount= BigDecimal.ZERO;
        //订单里程的总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        //起步价
        BigDecimal startingPrice = BigDecimal.ZERO;
        //套餐价
        BigDecimal  packagePrice  = BigDecimal.ZERO;
        //超里程价格
        BigDecimal  overMileagePrice  = BigDecimal.ZERO;
        //超时长价格
        BigDecimal  overtimeLongPrice  = BigDecimal.ZERO;
        //等待费
        BigDecimal  waitingFee  =BigDecimal.ZERO;
        //路桥费
        BigDecimal  roadBridgeFee  =BigDecimal.ZERO;
        if(orderSettlingInfoVo.getRoadBridgeFee().equals(null)){
            roadBridgeFee=BigDecimal.ZERO;
        }else{
            roadBridgeFee=orderSettlingInfoVo.getRoadBridgeFee();
        }
        //高速费
        BigDecimal  highSpeedFee  =BigDecimal.ZERO;
        if(orderSettlingInfoVo.getHighSpeedFee().equals(null)){
            highSpeedFee=BigDecimal.ZERO;
        }else{
            highSpeedFee=orderSettlingInfoVo.getHighSpeedFee();
        }

        //停车费
        BigDecimal  parkingRateFee  =BigDecimal.ZERO;
        if(orderSettlingInfoVo.getParkingRateFee().equals(null)){
            parkingRateFee=BigDecimal.ZERO;
        }else{
            parkingRateFee=orderSettlingInfoVo.getParkingRateFee();
        }
        //住宿费
        BigDecimal  hotelExpenseFee  =BigDecimal.ZERO;
        if(orderSettlingInfoVo.getHotelExpenseFee().equals(null)){
            hotelExpenseFee=BigDecimal.ZERO;
        }else{
            hotelExpenseFee=orderSettlingInfoVo.getHotelExpenseFee();
        }
        //餐饮费
        BigDecimal  restaurantFee  =BigDecimal.ZERO;
        if(orderSettlingInfoVo.getRestaurantFee().equals(null)){
            restaurantFee=BigDecimal.ZERO;
        }else{
            restaurantFee=orderSettlingInfoVo.getRestaurantFee();
        }
        if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())){
            //包车的情况下
            //套餐价
            packagePrice= costConfigInfo.getCombosPrice();
            //超里程数小于&&等于1  则不计算他的超里程费用
            if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                //超里程价格=（总里程-包含里程）* 超里程单价
                overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            }else {
                overMileagePrice=BigDecimal.ZERO;
            }
            //超时长价格小于&&等于1  则不计算他的超时长费用
            if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                overtimeLongPrice=BigDecimal.ZERO;
            }else{
                //超时长价格=(总时长-包含时长)* 超时长单价
                overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            }
            //总价=套餐价+（超里程价格）+（超时长价格)
            totalPrice =packagePrice.add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())){
            //预约
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            //超里程数小于&&等于1  则不计算他的超里程费用
            if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                //超里程价格=（总里程-包含里程）* 超里程单价
                overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            }else {
                overMileagePrice=BigDecimal.ZERO;
            }
            //超时长价格小于&&等于1  则不计算他的超时长费用
            if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                overtimeLongPrice=BigDecimal.ZERO;
            }else{
                //超时长价格=(总时长-包含时长)* 超时长单价
                overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            }
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
            //接机
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            //超里程数小于&&等于1  则不计算他的超里程费用
            if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                //超里程价格=（总里程-包含里程）* 超里程单价
                overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            }else {
                overMileagePrice=BigDecimal.ZERO;
            }
            //超时长价格小于&&等于1  则不计算他的超时长费用
            if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                overtimeLongPrice=BigDecimal.ZERO;
            }else{
                //超时长价格=(总时长-包含时长)* 超时长单价
                overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            }
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState())){
            //送机
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            //超里程数小于&&等于1  则不计算他的超里程费用
            if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                //超里程价格=（总里程-包含里程）* 超里程单价
                overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            }else {
                overMileagePrice=BigDecimal.ZERO;
            }
            //超时长价格小于&&等于1  则不计算他的超时长费用
            if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                overtimeLongPrice=BigDecimal.ZERO;
            }else{
                //超时长价格=(总时长-包含时长)* 超时长单价
                overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            }
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState())){
            //即时用车
            //起步价
            startingPrice=costConfigInfo.getStartPrice();
            // 等待费=（等待时长*等待单价）
            waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
            //超里程数小于&&等于1  则不计算他的超里程费用
            if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                //超里程价格=（总里程-包含里程）* 超里程单价
                overMileagePrice=(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
            }else {
                overMileagePrice=BigDecimal.ZERO;
            }
            //超时长价格小于&&等于1  则不计算他的超时长费用
            if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                overtimeLongPrice=BigDecimal.ZERO;
            }else{
                //超时长价格=(总时长-包含时长)* 超时长单价
                overtimeLongPrice=new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute());
            }
            //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
            totalPrice=startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice);
        }
        //订单各项费用科目和对应费用
        amount=totalPrice
                .add(roadBridgeFee
                .add(highSpeedFee)
                .add(parkingRateFee
                .add(hotelExpenseFee)
                .add(restaurantFee)));
        //返回所需要的详情
        orderSettlingInfoVo.setStartingPrice(startingPrice);
        orderSettlingInfoVo.setOverMileagePrice(overMileagePrice);
        orderSettlingInfoVo.setOvertimeLongPrice(overtimeLongPrice);
        orderSettlingInfoVo.setWaitingFee(waitingFee);
        orderSettlingInfoVo.setAmount(amount);
        return orderSettlingInfo;
    }
}