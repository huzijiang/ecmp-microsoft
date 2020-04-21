package com.hq.ecmp.mscore.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.dto.OrderEvaluationDto;
import com.hq.ecmp.mscore.mapper.EcmpUserFeedbackImageMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserFeedbackInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class EcmpUserFeedbackInfoServiceImpl implements IEcmpUserFeedbackInfoService
{
    @Resource
    private EcmpUserFeedbackInfoMapper ecmpUserFeedbackInfoMapper;
    @Resource
    private EcmpUserFeedbackImageMapper ecmpUserFeedbackImageMapper;
    @Resource
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpUserFeedbackInfo selectEcmpUserFeedbackInfoById(Long feedbackId)
    {
        return ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoById(feedbackId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpUserFeedbackInfo> selectEcmpUserFeedbackInfoList(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        return ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoList(ecmpUserFeedbackInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        ecmpUserFeedbackInfo.setCreateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackInfoMapper.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        ecmpUserFeedbackInfo.setUpdateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackInfoMapper.updateEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param feedbackIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackInfoByIds(Long[] feedbackIds)
    {
        return ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoByIds(feedbackIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackInfoById(Long feedbackId)
    {
        return ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoById(feedbackId);
    }

    @Override
    @Transactional
    public Long saveOrderEvaluation(OrderEvaluationDto evaluationDto, Long userId) throws Exception{
        List<EcmpUserFeedbackInfo> feedbackInfos = ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoList(new EcmpUserFeedbackInfo(evaluationDto.getOrderId()));
        if (CollectionUtils.isNotEmpty(feedbackInfos)){
            log.info("订单:"+evaluationDto.getOrderId()+"行程异议已存在");
            ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoById(feedbackInfos.get(0).getFeedbackId());
            List<EcmpUserFeedbackImage> ecmpUserFeedbackImages = ecmpUserFeedbackImageMapper.selectEcmpUserFeedbackImageList(new EcmpUserFeedbackImage(feedbackInfos.get(0).getFeedbackId()));
            if (CollectionUtils.isNotEmpty(ecmpUserFeedbackImages)){
                List<Long> collect = ecmpUserFeedbackImages.stream().map(EcmpUserFeedbackImage::getImageId).collect(Collectors.toList());
                Long[] longs = (Long[]) collect.toArray();
                ecmpUserFeedbackImageMapper.deleteEcmpUserFeedbackImageByIds(longs);
            }
        }
        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = new EcmpUserFeedbackInfo();
        BeanUtils.copyProperties(evaluationDto,ecmpUserFeedbackInfo);
        ecmpUserFeedbackInfo.setUserId(userId);
        log.info("订单:"+evaluationDto.getOrderId()+"异议参数"+evaluationDto.toString());
        int count = ecmpUserFeedbackInfoMapper.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
        if (count>0){
            if (StringUtils.isNotBlank(evaluationDto.getImgUrls())){
                EcmpUserFeedbackImage feedbackImage = new EcmpUserFeedbackImage();
                feedbackImage.setFeedbackId(ecmpUserFeedbackInfo.getFeedbackId());
                feedbackImage.setUserId(userId);
                String[] split = evaluationDto.getImgUrls().split(",");
                for (String url:split) {
                    feedbackImage.setImageUrl(url);
                    ecmpUserFeedbackImageMapper.insertEcmpUserFeedbackImage(feedbackImage);
                }
            }
        }
        OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
        stateTraceInfo.setOrderId(evaluationDto.getOrderId());
        stateTraceInfo.setContent(evaluationDto.getContent());
        stateTraceInfo.setCreateBy(String.valueOf(userId));
        stateTraceInfo.setCreateTime(new Date());
        stateTraceInfo.setState(OrderStateTrace.OBJECTION.getState());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(stateTraceInfo);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(evaluationDto.getOrderId());
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateTime(new Date());
        orderInfoMapper.updateOrderInfo(orderInfo);
        return ecmpUserFeedbackInfo.getFeedbackId();
    }
}
