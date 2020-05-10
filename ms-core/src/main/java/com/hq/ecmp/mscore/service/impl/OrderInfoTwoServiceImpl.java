package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderServiceAppraiseInfo;
import com.hq.ecmp.mscore.mapper.OrderServiceAppraiseInfoMapper;
import com.hq.ecmp.mscore.service.IOrderServiceAppraiseInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderInfoTwoServiceImpl implements OrderInfoTwoService
{

    /**
     * 公务取消订单
     * @param orderId
     * @param cancelReason
     */
    @Override
    public void cancelBusinessOrder(Long orderId, String cancelReason) {

    }
}
