package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import com.hq.ecmp.mscore.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ProjectInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param projectId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteProjectInfoById(Long projectId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectInfoByIds(Long[] projectIds);

    /**
     * 根据用户ID查询用户所包含的所有项目
     * @param userId
     * @return
     */
    List<ProjectInfo> getListByUserId(@Param("userId") Long userId,@Param("projectName") String projectName);

    List<ProjectInfoVO> getProjectList(@Param("search")String search,@Param("fatherProjectId")Long fatherProjectId);

    ProjectInfoVO getProjectInfo(Long projectId);

    List<ProjectInfo> checkProject(String date);

    UserVO findLeader(Long projectId);

    Long getProjectListCount(String search, Long fatherProjectId);
}
