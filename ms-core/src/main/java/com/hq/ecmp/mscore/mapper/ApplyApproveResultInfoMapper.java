package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.ApprovaReesultVO;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ApplyApproveResultInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyApproveResultInfoById(Long approveResultId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveResultIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteApplyApproveResultInfoByIds(Long[] approveResultIds);

    MessageDto getApproveMessage(Long userId);

    List<ApprovaReesultVO> selectResultList(@Param("userId") Long userId, @Param("state") String state);

    List<ApprovalInfoVO> getApproveResultList(@Param("applyId") Long applyId,@Param("approveTemplateId")  Long approveTemplateId);

    List<ApplyApproveResultInfo> selectApproveResultByNodeids(@Param("nextNodeIds") String nextNodeIds,@Param("state") String state);

    void insertList(List<ApplyApproveResultInfo> list);

    List<ApplyApproveResultInfo> selectByUserId(@Param("applyId") Long applyId,@Param("userId")  Long userId,@Param("state") String state);

    Integer getApprovePageCount(@Param("userId") Long userId, @Param("state") String state);

    /**
     * 直接调度修改审批
     * @param applyId
     * @param state
     * @param approveResult
     */
    void updateApproveState(@Param("applyId")Long applyId, @Param("state")String state,@Param("approveResult")String approveResult);
}
