package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IProjectUserRelationInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ProjectUserRelationInfo selectProjectUserRelationInfoById(Long projectId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ProjectUserRelationInfo> selectProjectUserRelationInfoList(ProjectUserRelationInfo projectUserRelationInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertProjectUserRelationInfo(ProjectUserRelationInfo projectUserRelationInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateProjectUserRelationInfo(ProjectUserRelationInfo projectUserRelationInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectUserRelationInfoByIds(Long[] projectIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectUserRelationInfoById(Long projectId);
}
