package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 成本计算器
 */
public interface CostCalculation {

    //计算成本
    OrderSettlingInfoVo calculator(List<CostConfigInfo> costConfigInfo, OrderSettlingInfoVo orderSettlingInfoVo);
}
