package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;

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

    /**
     * 获取审批通知
     * @param userId
     * @return
     */
    MessageDto getApproveMessage(Long userId);

    /**
     * 初始化审批流
     * @param applyId 申请id
     * @param regimenId 用车制度id
     */
    void initApproveResultInfo(Long applyId,Long regimenId,Long userId);

    List<ApprovalInfoVO> getApproveResultList(ApplyApproveResultInfo applyApproveResultInfo);
}
