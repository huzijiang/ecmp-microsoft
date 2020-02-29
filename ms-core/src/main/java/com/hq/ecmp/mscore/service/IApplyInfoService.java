package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;
import com.hq.ecmp.mscore.dto.ApplyTravelRequest;
import com.hq.ecmp.mscore.dto.JourneyCommitApplyDto;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IApplyInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApplyInfo selectApplyInfoById(Long applyId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApplyInfo(ApplyInfo applyInfo);

    /**
     * 修改申请表信息（撤销行程申请）
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApplyInfo(ApplyInfo applyInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyInfoByIds(Long[] applyIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param applyId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyInfoById(Long applyId);

    /**
     * 提交行程申请
     * @param journeyCommitApplyDto
     */
    public void applyCommit(JourneyCommitApplyDto journeyCommitApplyDto);

    /**
     * 提交公务行程申请
     * @param officialCommitApply
     */
    void applyOfficialCommit(ApplyOfficialRequest officialCommitApply);

    /**
     * 提交差旅行程申请
     * @param travelCommitApply
     */
    void applytravliCommit(ApplyTravelRequest travelCommitApply);
}
