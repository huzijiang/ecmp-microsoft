package com.hq.ecmp.mscore.service.impl;

import com.google.common.collect.Maps;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderPayInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderSettlingInfoMapper;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ghb
 * @description 支付表
 * @date 2020/5/7
 */
@Service
public class OrderPayInfoServiceImpl implements IOrderPayInfoService {

    @Resource
    private OrderPayInfoMapper orderPayInfoMapper;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Resource
    private RegimeInfoMapper regimeInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public int insertOrderPayInfo(OrderPayInfo orderPayInfo) {
        return orderPayInfoMapper.insertOrderPayInfo(orderPayInfo);
    }

    @Override
    public OrderPayInfo getOrderPayInfo(Long orderId) {
        return orderPayInfoMapper.getOrderPayInfo(orderId);
    }
    @Override
    public OrderPayInfo insertOrderPayAndSetting(Long orderNo,String amount,String distance,String duration,String json,String userId) {
        OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
        orderSettlingInfo.setOrderId(orderNo);
        orderSettlingInfo.setTotalMileage(new BigDecimal(distance).stripTrailingZeros());
        orderSettlingInfo.setTotalTime(new BigDecimal(duration).stripTrailingZeros());
        orderSettlingInfo.setAmount(new BigDecimal(amount).stripTrailingZeros());
        orderSettlingInfo.setAmountDetail(json);
        orderSettlingInfo.setCreateBy(CommonConstant.START);
        orderSettlingInfo.setCreateTime(DateUtils.getNowDate());
        orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
        //插入订单支付表
        OrderPayInfo orderPayInfo = new OrderPayInfo();
//        OrderSettlingInfo orderSettlingInfo1 = orderSettlingInfoMapper.selectOrderSettlingInfoById(orderNo);
        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        orderPayInfo.setPayId(substring);
        orderPayInfo.setBillId(orderSettlingInfo.getBillId());
        orderPayInfo.setOrderId(orderNo);
        orderPayInfo.setState(OrderPayConstant.PAID);
        orderPayInfo.setPayMode(OrderPayConstant.PAY_AFTER_STATEMENT);
        orderPayInfo.setAmount(new BigDecimal(amount).stripTrailingZeros());
        orderPayInfo.setCreateTime(DateUtils.getNowDate());
        orderPayInfo.setCreateBy(Long.parseLong(userId));
        this.insertOrderPayInfo(orderPayInfo);
        return orderPayInfo;
    }

    @Override
    public String checkOrderFeeOver(Long orderId,Long regimeId,Long applyUserId){
        //网约车是否限额
        //查询出用车制度表的限额额度，和限额类型
        Map<String,Object> map= Maps.newHashMap();
        OrderSettlingInfo orderSettlingInfo2 = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(orderId);
        RegimeVo regimeInfo = regimeInfoMapper.queryRegimeDetail(regimeId);
        BigDecimal limitMoney = regimeInfo.getLimitMoney();
        String limitType = regimeInfo.getLimitType();
        //按天
//        DecimalFormat df = new DecimalFormat("#0.00");
        int  isExcess=0;
        String  excessMoney=String.valueOf(CommonConstant.ZERO);
        if ("T001".equals(limitType)) {
            //查询出当前申请人
//            Long userId = orderInfo.getUserId();
            //根据订单号和当前申请人，得出当前申请人在当天一共申请的单量
            List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoByIdAllDay(applyUserId);
            //从订单结算表当中，查询出当前申请人在当天一共申请的单量的金额总和
            BigDecimal sum = BigDecimal.ZERO;
            List<Long> orderIds = orderInfos.stream().map(OrderInfo::getOrderId).collect(Collectors.toList());
            List<OrderSettlingInfo> list = orderSettlingInfoMapper.selectSettingInfoByOrderIds(orderIds);
            sum = list.stream().map(OrderSettlingInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 当前申请人在当天一共申请的单量的金额总和-限额=超额
            if (sum.compareTo(limitMoney) >= 0) {
                BigDecimal subtract = sum.subtract(limitMoney);
                isExcess=CommonConstant.ONE;
                excessMoney=subtract.stripTrailingZeros().toPlainString();
                //总额sum
            }
            // 按次数
        } else if ("T002".equals(limitType)) {
            //判断
            if (orderSettlingInfo2.getAmount().compareTo(limitMoney) > 0) {
                BigDecimal subtract = orderSettlingInfo2.getAmount().subtract(limitMoney);
                isExcess=CommonConstant.ONE;
                excessMoney=subtract.stripTrailingZeros().toPlainString();
            }
            //不限
        }else{
            return null;
        }

        return excessMoney;
    }
}
