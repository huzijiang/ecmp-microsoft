package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderPayInfo;



public interface IOrderPayInfoService {

    int insertOrderPayInfo(OrderPayInfo orderPayInfo);

    OrderPayInfo getOrderPayInfo(Long orderId);
}

