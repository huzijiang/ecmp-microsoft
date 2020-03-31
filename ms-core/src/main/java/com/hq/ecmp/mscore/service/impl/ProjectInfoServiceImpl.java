package com.hq.ecmp.mscore.service.impl;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.dto.ProjectUserDTO;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import com.hq.ecmp.mscore.vo.ProjectUserVO;
import com.hq.ecmp.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
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
    @Autowired
    private ProjectUserRelationInfoMapper projectUserRelationInfoMapper;

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

    /**
     * 根据用户ID查询用户所在的所有项目列表
     * @param userId
     * @return
     */
    @Override
    public List<ProjectInfo> getListByUserId(Long userId,String projectName) {
        return projectInfoMapper.getListByUserId(userId,projectName);
    }

    @Override
    public PageResult<ProjectInfoVO> getProjectList(Integer pageNum, Integer pageSize, String search, Long fatherProjectId) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectInfoVO> list= projectInfoMapper.getProjectList(search,fatherProjectId);
        Long count=projectInfoMapper.getProjectListCount(search,fatherProjectId);
        return new PageResult<ProjectInfoVO>(count,list);
    }

    @Override
    public ProjectInfoVO getProjectInfo(Long projectId) {
        return projectInfoMapper.getProjectInfo(projectId);
    }

    @Override
    public PageInfo<ProjectUserVO> getProjectUserList(Long projectId,int pageNum,int pageSize,String search) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectUserVO> list= projectUserRelationInfoMapper.getProjectUserList(projectId,search);
        return new PageInfo<>(list);
    }

    @Override
    public int removeProjectUser(ProjectUserDTO projectUserDTO) {
        return projectUserRelationInfoMapper.removeProjectUser(projectUserDTO.getProjectId(),projectUserDTO.getUserId());
    }

    @Override
    public int deleteProject(ProjectUserDTO projectUserDTO) {
        int i = projectInfoMapper.updateProjectInfo(new ProjectInfo(projectUserDTO.getProjectId(), -1));
        if (i>0){
            projectUserRelationInfoMapper.deleteProjectUserRelationInfoById(projectUserDTO.getProjectId());
        }
        return i;
    }

    @Override
    public void checkProject() {
        List<ProjectInfo> list= projectInfoMapper.checkProject(DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT,new Date()));
        if (CollectionUtils.isNotEmpty(list)){
            for (ProjectInfo info:list){
                info.setIsEffective(0);
                projectInfoMapper.updateProjectInfo(info);
            }
        }
    }
}
