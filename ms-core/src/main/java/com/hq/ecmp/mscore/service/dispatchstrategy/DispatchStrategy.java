package com.hq.ecmp.mscore.service.dispatchstrategy;

import com.hq.ecmp.mscore.dto.DispatchSendCarDto;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/25 17:50
 * @Version 1.0
 */
public interface DispatchStrategy {
    /**
     * 调度策略接口
     * @param dispatchSendCarDto
     * @exception
     */
     void dispatch(DispatchSendCarDto dispatchSendCarDto) throws Exception;
}
