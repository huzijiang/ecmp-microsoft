package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderPayInfo;



public interface IOrderPayInfoService {

    int insertOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfo(Long orderId);
    OrderPayInfo insertOrderPayAndSetting(Long orderNo,String amount,String distance,String duration,String json,String userId);
    String checkOrderFeeOver(Long orderId,Long regimeId,Long applyUserId) throws Exception;
    OrderPayInfo getOrderPayInfoByPayId(String payId);

    int updateOrderPayInfo(OrderPayInfo orderPayInfo);
}

