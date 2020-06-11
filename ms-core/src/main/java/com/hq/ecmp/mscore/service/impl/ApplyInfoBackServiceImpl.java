package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.reflect.TypeToken;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.bo.JourneyNodeBo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.RandomUtil;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.hq.ecmp.constant.CommonConstant.*;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class ApplyInfoBackServiceImpl implements ApplyInfoBackService
{
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private OrderDispatcheDetailInfoMapper dispatcheDetailInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;

    @Override
    public PageResult<UserApplySingleVo> getApplyListPage(SysUser loginUser, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserApplySingleVo> list=applyInfoMapper.getApplyListPage(loginUser.getDeptId(),null);
        PageInfo<UserApplySingleVo> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), info.getPages(), list);
    }

    @Override
    public ApplyInfoDetailVO getApplyInfoDetail(Long applyId,LoginUser loginUser) throws Exception{
        ApplyInfoDetailVO vo=new  ApplyInfoDetailVO();
        List<ApprovalDispatcherVO> approveList=new ArrayList<>();
        List<UserApplySingleVo> list=applyInfoMapper.getApplyListPage(null,applyId);
        if (CollectionUtils.isEmpty(list)){
            log.error("该申请单不存在:",applyId);
            throw new BaseException("该申请单不存在!");
        }
        UserApplySingleVo userApplySingleVo = list.get(0);
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(userApplySingleVo.getOrderId()));
        approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),userApplySingleVo.getApplyName(),userApplySingleVo.getApplyPhoneNumber(),"发起申请",userApplySingleVo.getCreateTime()));
        if (CollectionUtils.isEmpty(orderDispatcheDetailInfos)){
            vo.setApplyInfoDetail(userApplySingleVo);
            vo.setApproveList(approveList);
            return vo;
        }
        OrderDispatcheDetailInfo dispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        if (StringUtils.isNotBlank(dispatcheDetailInfo.getItIsUseInnerCarGroup())){
            userApplySingleVo.setCanUpdateDetail(1);
        }
        //获取内部车队
        List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.applySingleCarGroupList(IS_RETURN, "C000", loginUser.getUser().getDeptId());
        if (CollectionUtils.isEmpty(carGroupInfos)){
            throw new BaseException("内部车队为空");
        }
        CarGroupInfo carGroupInfo = carGroupInfos.get(0);
        DriverOrderInfoVO driverOrderInfoVO = orderInfoMapper.selectOrderDetail(userApplySingleVo.getOrderId());
        if (OrderState.ORDERDENIED.getState().equals(driverOrderInfoVO.getLabelState())){
            approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),carGroupInfo.getCarGroupName(),carGroupInfo.getTelephone(),"已驳回",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,driverOrderInfoVO.getLabelStateOptTime()),driverOrderInfoVO.getLabelStateContent()));
            vo.setApproveList(approveList);
            return vo;
        }
        if (StringUtils.isNotBlank(dispatcheDetailInfo.getItIsUseInnerCarGroup())){
            approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),carGroupInfo.getCarGroupName(),carGroupInfo.getTelephone(),"已处理",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,dispatcheDetailInfo.getUpdateTime())));
            if (OrderState.ORDEROVERTIME.getState().equals(driverOrderInfoVO.getLabelState())){
                approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),carGroupInfo.getCarGroupName(),carGroupInfo.getTelephone(),"未操作,已过期",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,driverOrderInfoVO.getLabelStateOptTime())));
            }else {
                if (dispatcheDetailInfo.getNextCarGroupId()!=null&&StringUtils.isNotBlank(dispatcheDetailInfo.getOuterCarGroupRefuseInfo())){
                    CarGroupInfo otherCarGroup = carGroupInfoMapper.selectCarGroupInfoById(dispatcheDetailInfo.getNextCarGroupId());
                    approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),otherCarGroup.getCarGroupName(),otherCarGroup.getTelephone(),"已处理",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,driverOrderInfoVO.getLabelStateOptTime())));
                }
            }
        }else{
            if (OrderState.ORDEROVERTIME.getState().equals(driverOrderInfoVO.getLabelState())){
                approveList.add(new ApprovalDispatcherVO(applyId,userApplySingleVo.getOrderNumber(),carGroupInfo.getCarGroupName(),carGroupInfo.getTelephone(),"未操作,已过期",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,driverOrderInfoVO.getLabelStateOptTime())));
            }
        }
        vo.setApplyInfoDetail(userApplySingleVo);
        vo.setApproveList(approveList);
        return vo;
    }
}
