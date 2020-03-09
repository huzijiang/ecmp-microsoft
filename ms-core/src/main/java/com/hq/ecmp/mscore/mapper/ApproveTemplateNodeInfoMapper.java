package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApproveTemplateNodeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ApproveTemplateNodeInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApproveTemplateNodeInfo selectApproveTemplateNodeInfoById(Long approveNodeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApproveTemplateNodeInfo> selectApproveTemplateNodeInfoList(ApproveTemplateNodeInfo approveTemplateNodeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApproveTemplateNodeInfoById(Long approveNodeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveNodeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteApproveTemplateNodeInfoByIds(Long[] approveNodeIds);

    String getListByNodeIds(List<Long> nodeIds);
}
