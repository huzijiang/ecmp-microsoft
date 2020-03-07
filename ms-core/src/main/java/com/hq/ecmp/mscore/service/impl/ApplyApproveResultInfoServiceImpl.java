package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.ApplyApproveResultInfoMapper;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ApplyApproveResultInfoServiceImpl implements IApplyApproveResultInfoService
{
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApplyApproveResultInfo selectApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoById(approveResultId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApplyApproveResultInfo> selectApplyApproveResultInfoList(ApplyApproveResultInfo applyApproveResultInfo)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoList(applyApproveResultInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setCreateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.insertApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setUpdateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.updateApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveResultIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoByIds(Long[] approveResultIds)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoByIds(approveResultIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoById(approveResultId);
    }

    @Override
    public MessageDto getApproveMessage(Long userId) {
        return applyApproveResultInfoMapper.getApproveMessage(userId);
    }
}
