package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import com.hq.ecmp.mscore.mapper.ApproveTemplateInfoMapper;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ApproveTemplateInfoServiceImpl implements IApproveTemplateInfoService
{
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApproveTemplateInfo selectApproveTemplateInfoById(Long approveTemplateId)
    {
        return approveTemplateInfoMapper.selectApproveTemplateInfoById(approveTemplateId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApproveTemplateInfo> selectApproveTemplateInfoList(ApproveTemplateInfo approveTemplateInfo)
    {
        return approveTemplateInfoMapper.selectApproveTemplateInfoList(approveTemplateInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo)
    {
        approveTemplateInfo.setCreateTime(DateUtils.getNowDate());
        return approveTemplateInfoMapper.insertApproveTemplateInfo(approveTemplateInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo)
    {
        approveTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        return approveTemplateInfoMapper.updateApproveTemplateInfo(approveTemplateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveTemplateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateInfoByIds(Long[] approveTemplateIds)
    {
        return approveTemplateInfoMapper.deleteApproveTemplateInfoByIds(approveTemplateIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateInfoById(Long approveTemplateId)
    {
        return approveTemplateInfoMapper.deleteApproveTemplateInfoById(approveTemplateId);
    }
}
