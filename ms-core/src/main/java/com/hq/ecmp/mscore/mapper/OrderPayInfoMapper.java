package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderPayInfo;


public interface OrderPayInfoMapper {

    int insertOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfo(Long orderId);
}
