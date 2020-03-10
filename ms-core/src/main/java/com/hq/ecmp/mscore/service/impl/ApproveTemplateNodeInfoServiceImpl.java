package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApproveTemplateNodeInfo;
import com.hq.ecmp.mscore.mapper.ApproveTemplateNodeInfoMapper;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ApproveTemplateNodeInfoServiceImpl implements IApproveTemplateNodeInfoService
{
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApproveTemplateNodeInfo selectApproveTemplateNodeInfoById(Long approveNodeId)
    {
        return approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoById(approveNodeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApproveTemplateNodeInfo> selectApproveTemplateNodeInfoList(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        return approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(approveTemplateNodeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        approveTemplateNodeInfo.setCreateTime(DateUtils.getNowDate());
        return approveTemplateNodeInfoMapper.insertApproveTemplateNodeInfo(approveTemplateNodeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        approveTemplateNodeInfo.setUpdateTime(DateUtils.getNowDate());
        return approveTemplateNodeInfoMapper.updateApproveTemplateNodeInfo(approveTemplateNodeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveNodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateNodeInfoByIds(Long[] approveNodeIds)
    {
        return approveTemplateNodeInfoMapper.deleteApproveTemplateNodeInfoByIds(approveNodeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateNodeInfoById(Long approveNodeId)
    {
        return approveTemplateNodeInfoMapper.deleteApproveTemplateNodeInfoById(approveNodeId);
    }

    @Override
    public String getListByNodeIds(List<Long> nodeIds) {

        return approveTemplateNodeInfoMapper.getListByNodeIds(nodeIds);
    }
}
