package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import org.springframework.stereotype.Service;

/**
 * @ClassName
 * @Description TODO 外部车外部司机策略
 * @Author yj
 * @Date 2020/5/25 17:53
 * @Version 1.0
 */
@Service("/outCarAndOutDriverStrategy")
public class OutCarAndOutDriverStrategy implements DispatchStrategy {

    @Override
    public void dispatch(DispatchSendCarDto dispatchSendCarDto) {

    }
}
