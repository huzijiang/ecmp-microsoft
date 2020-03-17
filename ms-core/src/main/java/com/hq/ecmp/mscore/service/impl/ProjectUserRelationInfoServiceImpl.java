package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ProjectUserRelationInfoServiceImpl implements IProjectUserRelationInfoService
{
    @Autowired
    private ProjectUserRelationInfoMapper projectUserRelationInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ProjectUserRelationInfo selectProjectUserRelationInfoById(Long projectId)
    {
        return projectUserRelationInfoMapper.selectProjectUserRelationInfoById(projectId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ProjectUserRelationInfo> selectProjectUserRelationInfoList(ProjectUserRelationInfo projectUserRelationInfo)
    {
        return projectUserRelationInfoMapper.selectProjectUserRelationInfoList(projectUserRelationInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertProjectUserRelationInfo(ProjectUserRelationInfo projectUserRelationInfo)
    {
        return projectUserRelationInfoMapper.insertProjectUserRelationInfo(projectUserRelationInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param projectUserRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateProjectUserRelationInfo(ProjectUserRelationInfo projectUserRelationInfo)
    {
        return projectUserRelationInfoMapper.updateProjectUserRelationInfo(projectUserRelationInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteProjectUserRelationInfoByIds(Long[] projectIds)
    {
        return projectUserRelationInfoMapper.deleteProjectUserRelationInfoByIds(projectIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteProjectUserRelationInfoById(Long projectId)
    {
        return projectUserRelationInfoMapper.deleteProjectUserRelationInfoById(projectId);
    }

    @Override
    public int insertProjectList(List<ProjectUserRelationInfo> list) {
       return projectUserRelationInfoMapper.insertProjectList(list);
    }

}
