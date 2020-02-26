package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ProjectInfo;

import java.util.List;

/**
 * 项目Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IProjectInfoService
{
    /**
     * 查询项目信息
     *
     * @param projectId 项目ID
     * @return 项目
     */
    public ProjectInfo selectProjectInfoById(Long projectId);

    /**
     * 查询项目信息列表
     *
     * @param projectInfo
     * @return 项目列表
     */
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo);

    /**
     * 新增项目
     *
     * @param projectInfo
     * @return 结果
     */
    public int insertProjectInfo(ProjectInfo projectInfo);

    /**
     * 修改项目信息
     *
     * @param projectInfo
     * @return 结果
     */
    public int updateProjectInfo(ProjectInfo projectInfo);

    /**
     * 批量删除项目
     *
     * @param projectIds 项目ID
     * @return 结果
     */
    public int deleteProjectInfoByIds(Long[] projectIds);

    /**
     * 删除项目信息
     *
     * @param projectId 项目ID
     * @return 结果
     */
    public int deleteProjectInfoById(Long projectId);
}
