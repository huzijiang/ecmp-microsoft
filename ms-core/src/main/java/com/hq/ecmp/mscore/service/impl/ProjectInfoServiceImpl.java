package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ProjectInfoServiceImpl implements IProjectInfoService
{
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ProjectInfo selectProjectInfoById(Long projectId)
    {
        return projectInfoMapper.selectProjectInfoById(projectId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param projectInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo)
    {
        return projectInfoMapper.selectProjectInfoList(projectInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param projectInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertProjectInfo(ProjectInfo projectInfo)
    {
        projectInfo.setCreateTime(DateUtils.getNowDate());
        return projectInfoMapper.insertProjectInfo(projectInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param projectInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateProjectInfo(ProjectInfo projectInfo)
    {
        projectInfo.setUpdateTime(DateUtils.getNowDate());
        return projectInfoMapper.updateProjectInfo(projectInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteProjectInfoByIds(Long[] projectIds)
    {
        return projectInfoMapper.deleteProjectInfoByIds(projectIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteProjectInfoById(Long projectId)
    {
        return projectInfoMapper.deleteProjectInfoById(projectId);
    }
}
