package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.OrderDispatcheDetailInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.TopDispatchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName
 * @Description TODO 内部车内部司机策略
 * @Author yj
 * @Date 2020/5/25 17:53
 * @Version 1.0
 */
@Service("/inCarAndDriverStrategy")
public class InCarAndDriverStrategy extends TopDispatchService implements DispatchStrategy  {


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
