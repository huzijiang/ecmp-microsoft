package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.vo.CancelOrderCostVO;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderInfoTwoService {

    CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason) throws Exception;
}
