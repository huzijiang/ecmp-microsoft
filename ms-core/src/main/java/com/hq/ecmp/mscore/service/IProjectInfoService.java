package com.hq.ecmp.mscore.service;

import com.hq.api.system.domain.SysUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.dto.ProjectUserDTO;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import com.hq.ecmp.mscore.vo.ProjectUserVO;

import java.util.List;
import java.util.Map;

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
    public List<ProjectInfo> getListByUserId(Long userId,String projectName,Long orgComcany);

    PageResult<ProjectInfoVO> getProjectList(Integer pageNum, Integer pageSize, String search, Long fatherProjectId,Long ownerCompany);

    ProjectInfoVO getProjectInfo(Long projectId);

    PageResult<ProjectUserVO> getProjectUserList(Long projectId,int pageNum,int pageSize,String search);

    int removeProjectUser(ProjectUserDTO projectUserDTO,Long userId);

    int deleteProject(ProjectUserDTO projectUserDTO)throws Exception;

    void checkProject();

    List<ProjectUserVO> getProjectUserInfo(Long projectId);

    OrgTreeVo selectProjectUserTree( Long projectId,String search);

    List<ProjectInfo> checkProjectCode(String projectCode, Long projectId,Long orgCompany);

    List<ProjectInfo> checkProjectName(String trim, Long orgComcany, Long projectId);

    List<ProjectUserVO> getUsersByOrg(Long projectId, String search, Long orgComcany);

    List<OrgTreeVo> selectProjectUserBySearch(Long projectId, String name);

    /**
     *申请人所属公司内的所有项目
     * @param ownerCompany
     * @return
     */
    List<ProjectInfoVO> selectProjects(Long ownerCompany);

    List<Map> buildProjectUserTree(Long projectId, String search, SysUser user);
//    List<OrgTreeVo> buildProjectUserTreeTest(Long projectId, String search, SysUser user);
}
