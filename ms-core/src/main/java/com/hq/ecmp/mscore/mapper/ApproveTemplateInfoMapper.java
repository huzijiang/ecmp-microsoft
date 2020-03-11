package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ApproveTemplateInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApproveTemplateInfoById(Long approveTemplateId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveTemplateIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteApproveTemplateInfoByIds(Long[] approveTemplateIds);
}
