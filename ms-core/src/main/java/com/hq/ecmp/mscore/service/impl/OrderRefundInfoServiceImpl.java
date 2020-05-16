package com.hq.ecmp.mscore.service.impl;

import com.google.common.collect.Maps;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import com.hq.ecmp.mscore.service.IOrderRefundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ghb
 * @description 退款表
 * @date 2020/5/16
 */
@Service
public class OrderRefundInfoServiceImpl implements IOrderRefundInfoService {

    @Autowired
    private OrderRefundInfoMapper oOrderRefundInfoMapper;

    @Override
    public int insertOrderRefundInfo(OrderRefundInfo orderPayInfo) {
        return oOrderRefundInfoMapper.insertOrderRefundInfo(orderPayInfo);
    }
}
