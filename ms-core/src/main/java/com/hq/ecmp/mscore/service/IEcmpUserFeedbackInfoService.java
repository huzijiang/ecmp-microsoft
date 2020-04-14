package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.dto.OrderEvaluationDto;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserFeedbackInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpUserFeedbackInfo selectEcmpUserFeedbackInfoById(Long feedbackId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpUserFeedbackInfo> selectEcmpUserFeedbackInfoList(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param feedbackIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoByIds(Long[] feedbackIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoById(Long feedbackId);

    Long saveOrderEvaluation(OrderEvaluationDto evaluationDto, Long userId) throws Exception;
}
