package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;

/**
 * 成本计算器
 */
public interface CostCalculation {

    //计算成本
    OrderSettlingInfoVo calculator(CostConfigInfo costConfigInfo,OrderSettlingInfoVo orderSettlingInfoVo);
}
