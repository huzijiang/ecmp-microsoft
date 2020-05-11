package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderPayInfo;


public interface OrderPayInfoMapper {

    int insertOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfo(Long orderId);

    int updateOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfoByPayId(String payId);
}
