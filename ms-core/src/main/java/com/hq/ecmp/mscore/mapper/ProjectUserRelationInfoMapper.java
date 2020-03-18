package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.vo.ProjectUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ProjectUserRelationInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectUserRelationInfoById(Long projectId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectUserRelationInfoByIds(Long[] projectIds);

    int insertProjectList(List<ProjectUserRelationInfo> list);

    List<ProjectUserVO> getProjectUserList(@Param("projectId")Long projectId,@Param("search") String search);

    int removeProjectUser(@Param("projectId")Long projectId, @Param("userId")Long userId);
}
