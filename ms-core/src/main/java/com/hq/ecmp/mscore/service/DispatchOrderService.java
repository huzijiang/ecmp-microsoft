package com.hq.ecmp.mscore.service;

import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.vo.OrderDispatcherVO;

public interface DispatchOrderService {

    OrderDispatcherVO getOrderDispatcher(Long orderId, LoginUser loginUser)throws Exception;
}
