package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.dto.MessageDto;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IApplyApproveResultInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApplyApproveResultInfo selectApplyApproveResultInfoById(Long approveResultId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApplyApproveResultInfo> selectApplyApproveResultInfoList(ApplyApproveResultInfo applyApproveResultInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveResultIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyApproveResultInfoByIds(Long[] approveResultIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyApproveResultInfoById(Long approveResultId);

    MessageDto getApproveMessage(Long userId);
}
