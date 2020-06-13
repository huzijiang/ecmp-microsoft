package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.domain.SysUser;
import com.hq.common.exception.BaseException;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.enumerate.CarGroupSourceEnum;
import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;
import com.hq.ecmp.mscore.mapper.CarGroupDispatcherInfoMapper;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.OrderDispatcheDetailInfoMapper;
import com.hq.ecmp.mscore.service.DispatchOrderService;
import com.hq.ecmp.mscore.vo.OrderDispatcherVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispatchOrderServiceImpl implements DispatchOrderService {

    @Resource
    private CarGroupDispatcherInfoMapper groupDispatcherInfoMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;
    @Resource
    private EcmpUserMapper userMapper;


    @Override
    public OrderDispatcherVO getOrderDispatcher(Long orderId, LoginUser loginUser) throws Exception{
        Long userId = loginUser.getUser().getUserId();
        OrderDispatcherVO vo=new OrderDispatcherVO();
        List<CarGroupInfo> carGroupDispatcherInfos = carGroupInfoMapper.selectListByDispatcherId(userId);
        if (CollectionUtils.isEmpty(carGroupDispatcherInfos)){
            throw new BaseException("当前用户不是调度员");
        }
        List<CarGroupInfo> collect = carGroupDispatcherInfos.stream().filter(p -> CarGroupSourceEnum.INNER_CAR_GROUP.getCode().equals(p.getItIsInner())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            vo.setDispatcherFlag(CarGroupSourceEnum.OUTER_CAR_GROUP.getCode());
            //获取内部调度员
            OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfoMapper.selectDispatcheInfo(orderId);
            if (orderDispatcheDetailInfo!=null){
                EcmpUser ecmpUser = userMapper.selectEcmpUserById(orderDispatcheDetailInfo.getInnerDispatcher());
                if (ecmpUser!=null){
                    vo.setInnerDispatcherName(ecmpUser.getNickName());
                    vo.setInnerDispatcherPhone(ecmpUser.getPhonenumber());
                }
            }
        }else{
            vo.setDispatcherFlag(CarGroupSourceEnum.INNER_CAR_GROUP.getCode());
        }

        return vo;
    }
}
