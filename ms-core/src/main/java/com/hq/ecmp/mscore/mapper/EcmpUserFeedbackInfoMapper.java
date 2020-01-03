package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpUserFeedbackInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoById(Long feedbackId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param feedbackIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoByIds(Long[] feedbackIds);
}
