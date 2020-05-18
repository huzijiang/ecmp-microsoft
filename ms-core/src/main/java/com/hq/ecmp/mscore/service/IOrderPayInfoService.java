package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderPayInfo;

import java.math.BigDecimal;


public interface IOrderPayInfoService {

    int insertOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfo(Long orderId);
    OrderPayInfo insertOrderPayAndSetting(Long orderNo, BigDecimal amount, String distance, String duration, String json, Long userId,BigDecimal overMoney);
    BigDecimal checkOrderFeeOver(BigDecimal amount,Long regimeId,Long applyUserId) throws Exception;
    OrderPayInfo getOrderPayInfoByPayId(String payId);

    int updateOrderPayInfo(OrderPayInfo orderPayInfo);
}

