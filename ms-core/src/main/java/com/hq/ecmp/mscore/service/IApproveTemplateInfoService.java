package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IApproveTemplateInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApproveTemplateInfo selectApproveTemplateInfoById(Long approveTemplateId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApproveTemplateInfo> selectApproveTemplateInfoList(ApproveTemplateInfo approveTemplateInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveTemplateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApproveTemplateInfoByIds(Long[] approveTemplateIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApproveTemplateInfoById(Long approveTemplateId);

    List<ApprovaTemplateVO> getTemplateList();

    ApprovaTemplateVO flowTemplateDetail(Long templateId);

    void deleteFlow(Long approveTemplateId) throws Exception;
}
