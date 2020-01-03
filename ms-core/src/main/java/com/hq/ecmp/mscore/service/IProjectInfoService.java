package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ProjectInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IProjectInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ProjectInfo selectProjectInfoById(Long projectId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param projectInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param projectInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertProjectInfo(ProjectInfo projectInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param projectInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateProjectInfo(ProjectInfo projectInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectInfoByIds(Long[] projectIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectInfoById(Long projectId);
}
