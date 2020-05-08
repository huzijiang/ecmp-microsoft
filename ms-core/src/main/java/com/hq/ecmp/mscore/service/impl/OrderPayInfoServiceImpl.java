package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.OrderPayInfo;
import com.hq.ecmp.mscore.mapper.OrderPayInfoMapper;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ghb
 * @description 支付表
 * @date 2020/5/7
 */
@Service
public class OrderPayInfoServiceImpl implements IOrderPayInfoService {

    @Autowired
    private OrderPayInfoMapper orderPayInfoMapper;

    @Override
    public int insertOrderPayInfo(OrderPayInfo orderPayInfo) {
        return orderPayInfoMapper.insertOrderPayInfo(orderPayInfo);
    }

}
