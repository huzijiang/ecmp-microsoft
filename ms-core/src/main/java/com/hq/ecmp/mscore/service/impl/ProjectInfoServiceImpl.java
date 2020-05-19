package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.hq.api.system.domain.SysUser;
import com.hq.api.system.mapper.SysUserMapper;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.TreeUtil;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.dto.ProjectUserDTO;
import com.hq.ecmp.mscore.mapper.EcmpOrgMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hq.ecmp.constant.CommonConstant.ONE;
import static com.hq.ecmp.constant.CommonConstant.ZERO;

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
    @Autowired(required = false)
    private SysUserMapper userMapper;

    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private RedisUtil redisUtil;

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
    public List<ProjectInfo> getListByUserId(Long userId,String projectName,Long orgComcany) {
        return projectInfoMapper.getListByUserId(userId,projectName,orgComcany);
    }

    @Override
    public PageResult<ProjectInfoVO> getProjectList(Integer pageNum, Integer pageSize, String search, Long fatherProjectId,Long ownerCompany) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectInfoVO> list= projectInfoMapper.getProjectList(search,fatherProjectId,ownerCompany);
        if(CollectionUtils.isNotEmpty(list)){
            for (ProjectInfoVO vo:list){
                int isdeleteCount=  projectInfoMapper.selectChildProject(vo.getProjectId());
                if (isdeleteCount>0){
                    vo.setIsCanDelete(ZERO);
                }else{
                    vo.setIsCanDelete(ONE);
                }
            }
        }
        Long count=projectInfoMapper.getProjectListCount(search,fatherProjectId);
        return new PageResult<ProjectInfoVO>(count,list);
    }

    @Override
    public ProjectInfoVO getProjectInfo(Long projectId) {
        return projectInfoMapper.getProjectInfo(projectId);
    }

    @Override
    public PageResult<ProjectUserVO> getProjectUserList(Long projectId,int pageNum,int pageSize,String search) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectUserVO> list= projectUserRelationInfoMapper.getProjectUserList(projectId,search);
        Long count=projectUserRelationInfoMapper.getProjectUserListCount(projectId,search);
        return new PageResult<>(count,list);
    }

    @Override
    @Transactional
    public int removeProjectUser(ProjectUserDTO projectUserDTO,Long userId) {
        if (projectUserDTO.getUserId()==null||projectUserDTO.getProjectId()==null){
            return 0;
        }
        int i = projectUserRelationInfoMapper.removeProjectUser(projectUserDTO.getProjectId(), projectUserDTO.getUserId());
        if (i>0){
            ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(projectUserDTO.getProjectId());
            if (projectInfo!=null&& ZERO==projectInfo.getIsAllUserUse()){
                projectInfo.setIsAllUserUse(ONE);
                projectInfo.setUpdateBy(String.valueOf(userId));
                projectInfo.setUpdateTime(new Date());
                projectInfoMapper.updateProjectInfo(projectInfo);
            }
        }
        return i;
    }

    @Override
    public int deleteProject(ProjectUserDTO projectUserDTO) throws Exception{
        int isdeleteCount=  projectInfoMapper.selectChildProject(projectUserDTO.getProjectId());
        if (isdeleteCount>0){
            throw new Exception("该项目:"+projectUserDTO.getProjectId()+"下有");
        }
        int i = projectInfoMapper.updateProjectInfo(new ProjectInfo(projectUserDTO.getProjectId(), 4));
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
                info.setIsEffective(ONE);
                projectInfoMapper.updateProjectInfo(info);
            }
        }
    }

    @Override
    public List<ProjectUserVO> getProjectUserInfo(Long projectId) {
        List<ProjectUserVO> list= projectUserRelationInfoMapper.getProjectUserList(projectId,null);
        return list;
    }


        @Override
    public OrgTreeVo selectProjectUserTree(Long projectId,String search) {
        ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(projectId);
        Long orgId=null;
        if (projectInfo!=null&&projectInfo.getIsAllUserUse()!=ZERO){
            orgId=projectInfo.getOwnerCompany();
        }
        OrgTreeVo orgTreeVo = ecmpOrgMapper.selectDeptTree(orgId,null);
        List<UserTreeVo> userList =ecmpUserMapper.selectUserListByDeptIdAndProjectId(projectId,search);
        OrgTreeVo childNode = getChildNode(orgTreeVo, userList);

        return childNode;
    }
    @Override
    public List<Map> buildProjectUserTree(Long projectId, String search, SysUser user) {
        List<Map> maps = new ArrayList<>();
        ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(projectId);
        Long orgId=null;
        if (projectInfo!=null){
            orgId=projectInfo.getOwnerCompany();
        }else{
            orgId=user.getOwnerCompany();
        }
        maps.addAll(ecmpUserMapper.selectUserListByProjectId(projectId,search,orgId));
        List<String> deptIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(maps)){
            maps.stream().forEach(x->deptIds.add(x.get("pid").toString()));
        }
        while (deptIds.size()>0){
            List<Map> list = userMapper.selectDeptInfoByIds(deptIds);
            maps.addAll(list);
            deptIds.clear();
            list.stream().forEach(x->deptIds.add(x.get("pid").toString()));
        }
        Collections.reverse(maps);
        List<Map> tree = TreeUtil.getTreeByList(maps);
        return tree;
    }

    @Override
    public List<ProjectInfo> checkProjectCode(String projectCode, Long projectId,Long orgCompany) {
        return projectInfoMapper.checkProjectCode(projectCode,projectId,orgCompany);
    }

    @Override
    public List<ProjectInfo> checkProjectName(String name, Long orgComcany, Long projectId) {
        return projectInfoMapper.checkProjectName(name,orgComcany,projectId);
    }

    /**
     *获取当前公司下的所有员工
     * @param projectId
     * @param search
     * @param orgComcany
     * @return
     */
    @Override
    public List<ProjectUserVO> getUsersByOrg(Long projectId, String search, Long orgComcany) {
        return ecmpUserMapper.getUsersByCompany(search,orgComcany);
    }

    @Override
    public List<OrgTreeVo> selectProjectUserBySearch(Long projectId, String name) {
        ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(projectId);
        Long orgId=null;
        if (projectInfo!=null&&projectInfo.getIsAllUserUse()!=ZERO){
            orgId=projectInfo.getOwnerCompany();
        }
        List<OrgTreeVo> vo=projectUserRelationInfoMapper.selectProjectUserBySearch(projectId,name,orgId);
        return vo;
    }

    /**
     * 申请人所属公司内的所有项目
     * @param ownerCompany
     * @return
     */
    @Override
    public List<ProjectInfoVO> selectProjects(Long ownerCompany) {

        return projectInfoMapper.selectProjects(ownerCompany);
    }

    private OrgTreeVo getChildNode(OrgTreeVo orgTreeVos, List<UserTreeVo> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        List<OrgTreeVo> children = orgTreeVos.getChildren();
        for (UserTreeVo vo:userList) {
            if (orgTreeVos.getId().equals(vo.getDeptId())&&String.valueOf(ZERO).equals(orgTreeVos.getType()) ){
                OrgTreeVo userVo=new OrgTreeVo();
                userVo.setParentId(vo.getDeptId());
                userVo.setId(vo.getUserId());
                userVo.setShowname(vo.getNickName());
                userVo.setType(CommonConstant.SWITCH_OFF);
                children.add(userVo);
            }
        }
        orgTreeVos.setChildren(children);
        if (CollectionUtils.isEmpty(orgTreeVos.getChildren())) {
            return orgTreeVos;
        } else {
            for (OrgTreeVo deptAndUser : orgTreeVos.getChildren()) {
                getChildNode(deptAndUser, userList);
            }
        }
        return orgTreeVos;
    }
}
