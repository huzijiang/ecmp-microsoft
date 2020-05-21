package com.hq.ecmp.mscore.service;

import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderInfoTwoService {

    CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason,Long loginUserId) throws Exception;

    List<RunningOrderVo> runningOrder(Long orderId);

    PageResult<DispatchVo> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser);

    OrderReassignVO reassignDetail(Long orderNo, Long driverId);
}
