package com.hq.ecmp.mscore.service;

import com.github.pagehelper.PageInfo;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.vo.*;

import java.util.Date;
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

   /* *//**
     * 提交行程申请
     * @param journeyCommitApplyDto
     *//*
    public void applyCommit(JourneyCommitApplyDto journeyCommitApplyDto);*/

    /**
     * 提交公务行程申请
     * @param officialCommitApply
     */
    ApplyVO applyOfficialCommit(ApplyOfficialRequest officialCommitApply) throws Exception;

    public List<Long> initialOfficialPowerAndApprovalFlow(LoginUser loginUser,ApplyOfficialRequest officialCommitApply, Long journeyId,  Long applyId, Long userId) throws Exception;

    /**
     * 提交差旅行程申请
     * @param travelCommitApply
     */
   public ApplyVO applytravliCommit(ApplyTravelRequest travelCommitApply);

    public void initialPowerAndApprovalFlow(ApplyTravelRequest travelCommitApply, Long journeyId, Long applyId) throws Exception;

    /**
     *
     * 分页查询用户申请列表
     * @param userId
     * @param pageNum
     * @return
     */
    PageResult<ApplyInfoDTO> selectApplyInfoListByPage(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 查询申请单详情
     * @param applyId
     * @return
     */
    ApplyDetailVO selectApplyDetail(Long applyId);

    List<MessageDto> getOrderCount(Long userId);

    MessageDto getApplyMessage(Long userId);

    int getApplyApproveCount(Long userId);

    List<ApprovaReesultVO> getApprovePage(int pageIndex,int pageSize,Long userId);

    Integer getApprovePageCount(Long userId);
    //获取审批流信息
    List<ApprovalListVO> getApproveList(String applyUser, String applyMobile, Long applyId, Date time);

    int updateApplyState(Long applyId,String applyState,String approveState,Long userId) throws Exception;
   void updateApproveResult(Long applyId,String state,Long userId) throws Exception;

    void checkApplyExpired();
}
