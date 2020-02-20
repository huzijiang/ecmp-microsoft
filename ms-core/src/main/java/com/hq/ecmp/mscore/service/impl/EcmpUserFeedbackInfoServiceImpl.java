package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.mapper.EcmpUserFeedbackInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserFeedbackInfoServiceImpl implements IEcmpUserFeedbackInfoService
{
    @Autowired
    private EcmpUserFeedbackInfoMapper ecmpUserFeedbackInfoMapper;

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
}
