package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.TopDispatchService;
import org.springframework.stereotype.Service;

/**
 * @ClassName
 * @Description TODO 内部车自驾策略
 * @Author yj
 * @Date 2020/5/25 17:53
 * @Version 1.0
 */
@Service("/inCarAndSelfDriverStrategy")
public class InCarAndSelfDriverStrategy extends TopDispatchService implements DispatchStrategy {

    @Override
    public void dispatch(DispatchSendCarDto dispatchSendCarDto) {
        this.disBusiness(dispatchSendCarDto);
    }

    @Override
    public void judgeIsFinish(DispatchSendCarDto dispatchSendCarDto) {
        if(dispatchSendCarDto.getInOrOut() == 1){
            dispatchSendCarDto.setIsFinishDispatch(1);
        }
    }
}
