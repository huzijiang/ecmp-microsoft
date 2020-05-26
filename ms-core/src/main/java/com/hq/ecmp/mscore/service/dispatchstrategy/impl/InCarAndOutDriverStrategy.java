package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.TopDispatchService;
import org.springframework.stereotype.Service;

/**
 * @ClassName
 * @Description TODO 内部车外部司机策略
 * @Author yj
 * @Date 2020/5/25 17:53
 * @Version 1.0
 */
@Service("/inCarAndOutDriverStrategy")
public class InCarAndOutDriverStrategy extends TopDispatchService implements DispatchStrategy {

    @Override
    public void dispatch(DispatchSendCarDto dispatchSendCarDto) {
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
