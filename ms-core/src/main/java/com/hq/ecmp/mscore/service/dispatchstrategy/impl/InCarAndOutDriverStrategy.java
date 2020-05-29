package com.hq.ecmp.mscore.service.dispatchstrategy.impl;

import com.hq.common.utils.DateUtils;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.SmsTemplateConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.TopDispatchService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description TODO 内部车外部司机策略
 * @Author yj
 * @Date 2020/5/25 17:53
 * @Version 1.0
 */
@Service("inCarAndOutDriverStrategy")
public class InCarAndOutDriverStrategy extends TopDispatchService implements DispatchStrategy {

    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Resource
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private ISmsTemplateInfoService iSmsTemplateInfoService;


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
