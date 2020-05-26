package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.TopDispatchService;
import org.springframework.stereotype.Service;

/**
 * @ClassName
 * @Description TODO 内部司机外部车策略
 * @Author yj
 * @Date 2020/5/25 17:55
 * @Version 1.0
 */
@Service("inDriverAndOutCarStrategy")
public class InDriverAndOutCarStrategy extends TopDispatchService implements DispatchStrategy {
    @Override
    public void dispatch(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        this.disBusiness(dispatchSendCarDto);
    }

    @Override
    public void judgeIsFinish(DispatchSendCarDto dispatchSendCarDto) {
        if(dispatchSendCarDto.getInOrOut() == 1){
            dispatchSendCarDto.setIsFinishDispatch(2);
        }else if(dispatchSendCarDto.getInOrOut() == 2){
            dispatchSendCarDto.setIsFinishDispatch(1);
        }
    }
}
