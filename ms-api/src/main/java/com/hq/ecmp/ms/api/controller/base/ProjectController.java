package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.ms.api.dto.base.ProjectDto;
import com.hq.ecmp.ms.api.dto.base.ProjectUserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.ProjectInfoDTO;
import com.hq.ecmp.mscore.dto.ProjectUserDTO;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import com.hq.ecmp.mscore.vo.ProjectUserVO;
import com.hq.ecmp.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hq.common.enums.ErrorCodeEnum.SUCCESS;
import static com.hq.ecmp.constant.CommonConstant.ZERO;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IProjectUserRelationInfoService iProjectUserRelationInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IEcmpOrgService ecmpOrgService;

    /**
     * 项目下拉选
     * @return
     */
    @ApiOperation(value = "selectProjectsList",notes = "项目下拉选 ",httpMethod ="POST")
    @PostMapping("/selectProjectsList")
    public ApiResponse<List<ProjectInfo>> selectProjectsList(){
        List<ProjectInfo> projectInfoList = iProjectInfoService.selectProjectInfoList(new ProjectInfo(CommonConstant.ZERO));
        if (CollectionUtils.isNotEmpty(projectInfoList)){
            return ApiResponse.success(projectInfoList);
        }else {
            return ApiResponse.error("未查询到项目信息列表");
        }
    }
    /**
     * 查询用户所在的所有项目列表
     * @return
     */
    @ApiOperation(value = "getListByUser",notes = "查询用户所有的项目列表",httpMethod ="POST")
    @PostMapping("/getListByUser")
    public ApiResponse<List<ProjectInfo>> getListByUser(@RequestBody ProjectDto projectDto){
        //根据用户Id查询项目对象
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long orgComcany=null;
        EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
        if (ecmpOrg!=null){
            orgComcany=ecmpOrg.getDeptId();
        }
        List<ProjectInfo> projectInfos = iProjectInfoService.getListByUserId(loginUser.getUser().getUserId(),projectDto.getProjectName(),orgComcany);
        if(CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.success(projectInfos);
        }else {
            return ApiResponse.error("未查询到项目编号");
        }
    }

    /**
     * 校验项目编号
     * @return
     */
    @ApiOperation(value = "checkProjectCode",notes = "校验项目编号",httpMethod ="POST")
    @PostMapping("/checkProjectCode")
    public ApiResponse checkProjectCode(@RequestBody ProjectInfoDTO projectInfoDto){
        if (StringUtils.isEmpty(projectInfoDto.getProjectCode())){
            return ApiResponse.success();
        }
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long orgComcany=null;
        if (projectInfoDto.getProjectId()==null){
            EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
            if (ecmpOrg!=null){
                orgComcany=ecmpOrg.getDeptId();
            }
        }else{
            ProjectInfo projectInfo = iProjectInfoService.selectProjectInfoById(projectInfoDto.getProjectId());
            orgComcany=projectInfo.getOwnerCompany();
        }
        List<ProjectInfo> projectInfos = iProjectInfoService.checkProjectCode(projectInfoDto.getProjectCode(),projectInfoDto.getProjectId(),orgComcany);
        if (CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.success(SUCCESS.getMsg(),"该项目编号已存在,不可重复录入!");
        }
        return ApiResponse.success();
    }
    /**
     * 校验项目名称
     * @return
     */
    @ApiOperation(value = "checkProjectName",notes = "校验项目名称",httpMethod ="POST")
    @PostMapping("/checkProjectName")
    public ApiResponse checkProjectName(@RequestBody ProjectInfoDTO projectInfoDto){
        if (StringUtils.isEmpty(projectInfoDto.getName())){
            return ApiResponse.success();
        }
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long orgComcany=null;
        if (projectInfoDto.getProjectId()==null){
            EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
            if (ecmpOrg!=null){
                orgComcany=ecmpOrg.getDeptId();
            }
        }else{
            ProjectInfo projectInfo = iProjectInfoService.selectProjectInfoById(projectInfoDto.getProjectId());
            orgComcany=projectInfo.getOwnerCompany();
        }
        List<ProjectInfo> projectInfos = iProjectInfoService.checkProjectName(projectInfoDto.getName().trim(),orgComcany,projectInfoDto.getProjectId());
        if (CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.success(SUCCESS.getMsg(),"该项目名称已存在,不可重复录入!");
        }
        return ApiResponse.success();
    }

//    /**
//     * 获取项目主管列表
//     * @return
//     */
//    @ApiOperation(value = "getProjectDirector",notes = "获取项目主管列表",httpMethod ="POST")
//    @PostMapping("/getProjectDirector")
//    public ApiResponse getProjectDirector(@RequestBody ProjectInfoDTO projectInfoDto){
//        return ApiResponse.success();
//    }

    /**
     * 获取项目列表
     * @return
     */
    @ApiOperation(value = "getProjectList",notes = "获取项目列表",httpMethod ="POST")
    @PostMapping("/getProjectList")
    public ApiResponse<PageResult<ProjectInfoVO>> getProjectList(@RequestBody PageRequest page){
        //获取当前登录用户的公司ID
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ownerCompany = loginUser.getUser().getOwnerCompany();
        PageResult<ProjectInfoVO> pageInfo= iProjectInfoService.getProjectList(page.getPageNum(),page.getPageSize(),page.getSearch(),page.getFatherProjectId(),ownerCompany);
        return ApiResponse.success(pageInfo);
    }

    /**
     * 申请人所属公司内的所有项目
     * @return
     */
    @ApiOperation(value = "selectProjects",notes = "申请人所属公司内的所有项目",httpMethod ="POST")
    @PostMapping("/selectProjects")
    public ApiResponse<List<ProjectInfoVO>> selectProjects(){
        //获取当前登录用户的公司ID
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ownerCompany = loginUser.getUser().getDept().getCompanyId();
        List<ProjectInfoVO> pageInfo= iProjectInfoService.selectProjects(Long.valueOf(ownerCompany));

        return ApiResponse.success(pageInfo);
    }
    /**
     * 新增项目
     * @return
     */
    @ApiOperation(value = "createProject",notes = "新增项目",httpMethod ="POST")
    @PostMapping("/createProject")
    public ApiResponse createProject(@RequestBody ProjectInfoDTO projectInfoDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(projectInfoDto,projectInfo);
        projectInfo.setIsEffective(ZERO);
        EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
        if (ecmpOrg!=null){
            projectInfo.setOwnerCompany(ecmpOrg.getDeptId());
        }
        projectInfo.setOwnerOrg(loginUser.getUser().getDeptId());
        if (projectInfoDto.getFatherProjectId()==null){
            projectInfo.setFatherProjectId(0L);
        }
        projectInfo.setCreateBy(String.valueOf(loginUser.getUser().getUserId()));
        projectInfo.setCreateTime(new Date());
        int i = iProjectInfoService.insertProjectInfo(projectInfo);
//        if (i>0){
//            if (projectInfoDto.getIsAllUserUse()==ZERO){//全部员工
//                this.saveUserProject(projectInfo.getProjectId());
//            }
//        }
        return ApiResponse.success("新建项目成功");
    }

    @ApiOperation(value = "getProjectInfo",notes = "详情",httpMethod ="POST")
    @PostMapping("/getProjectInfo")
    @Transactional
    public ApiResponse<ProjectInfoVO> getProjectInfo(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfoVO VO=iProjectInfoService.getProjectInfo(projectInfoDto.getProjectId());
        return ApiResponse.success(VO);
    }

    private void saveUserProject(Long projectId){
        if(projectId==null){
            return;
        }
        List<EcmpUser> ecmpUsers = ecmpUserService.selectEcmpUserList(new EcmpUser(CommonConstant.SWITCH_ON,CommonConstant.SWITCH_ON));
        List<ProjectUserRelationInfo> list=new ArrayList<>();
        for(EcmpUser user:ecmpUsers){
            list.add(new ProjectUserRelationInfo(projectId,user.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(list)){
            List<ProjectUserRelationInfo> projectUserRelationInfo = iProjectUserRelationInfoService.selectProjectUserRelationInfoList(new ProjectUserRelationInfo(projectId));
            list.addAll(projectUserRelationInfo);
            List<ProjectUserRelationInfo> collect = list.stream().distinct().collect(Collectors.toList());
            iProjectUserRelationInfoService.insertProjectList(collect);
        }
    }
    @ApiOperation(value = "editProject",notes = "编辑",httpMethod ="POST")
    @PostMapping("/editProject")
    @Transactional
    public ApiResponse editProject(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfo projectInfo = new ProjectInfo();
        if (projectInfoDto.getProjectId()==null||projectInfoDto.getProjectId()==0){
            return ApiResponse.error("项目id不可为空");
        }
        ProjectInfo oldInfo = iProjectInfoService.selectProjectInfoById(projectInfoDto.getProjectId());
        int isAlluser=oldInfo.getIsAllUserUse();
        BeanUtils.copyProperties(projectInfoDto,projectInfo);
        if (projectInfoDto.getIsFinite()==ZERO){//无限
            projectInfo.setStartDate(null);
            projectInfo.setCloseDate(null);
        }
        int i = iProjectInfoService.updateProjectInfo(projectInfo);
        if (i>0){
            if (isAlluser!=projectInfoDto.getIsAllUserUse()){
                if (projectInfoDto.getIsAllUserUse()==ZERO){//全部员工
                    iProjectUserRelationInfoService.deleteProjectUserRelationInfoById(projectInfoDto.getProjectId());
                }
            }
        }
        return ApiResponse.success("编辑项目成功");
    }


    @ApiOperation(value = "getProjectUserList",notes = "获取成员列表",httpMethod ="POST")
    @PostMapping("/getProjectUserList")
    public ApiResponse<PageResult<ProjectUserVO>> getProjectUserList(@RequestBody PageRequest pageRequest){
        PageResult<ProjectUserVO> VO=iProjectInfoService.getProjectUserList(pageRequest.getFatherProjectId(),pageRequest.getPageNum(),pageRequest.getPageSize(),pageRequest.getSearch());
        return ApiResponse.success(VO);
    }

    /**
     * 成员树
     * @return*/
    @ApiOperation(value = "显示部门及员工树",notes = "显示部门及员工树",httpMethod ="POST")
    @PostMapping("/selectProjectUserTree")
    public ApiResponse<List<Map>> selectProjectUserTree(@RequestBody ProjectInfoDTO projectInfoDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<Map> lsit = iProjectInfoService.buildProjectUserTree(projectInfoDto.getProjectId(),projectInfoDto.getSearch(),loginUser.getUser());
        return ApiResponse.success(lsit);


    }

    @ApiOperation(value = "getProjectUserInfo",notes = "根据项目id获取已绑定所有成员列表",httpMethod ="POST")
    @RequestMapping("/getProjectUserInfo")
    public ApiResponse<List<ProjectUserVO>> getProjectUserInfo(@RequestBody ProjectDto projectDto){
//        List<ProjectUserVO> users=iProjectInfoService.getProjectUserInfo(projectId);
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long orgComcany=null;
        if (projectDto.getProjectId()==null){
            EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
            if (ecmpOrg!=null){
                orgComcany=ecmpOrg.getDeptId();
            }
        }else{
            ProjectInfo projectInfo = iProjectInfoService.selectProjectInfoById(projectDto.getProjectId());
            if (projectInfo!=null){
                orgComcany=projectInfo.getOwnerCompany();
            }
        }
        List<ProjectUserVO> projectUserInfo = iProjectInfoService.getUsersByOrg(projectDto.getProjectId(),projectDto.getSearch(),orgComcany);
        return ApiResponse.success(projectUserInfo);
    }

    @ApiOperation(value = "removeProjectUser",notes = "移除成员",httpMethod ="POST")
    @PostMapping("/removeProjectUser")
    @Transactional
    public ApiResponse removeProjectUser(@RequestBody ProjectUserDTO projectUserDTO){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        int count=iProjectInfoService.removeProjectUser(projectUserDTO,loginUser.getUser().getUserId());
        if (count>0){
//            redisUtil.delKey(String.format(PROJECT_USER_TREE, projectUserDTO.getProjectId()));
            return ApiResponse.success();
        }else {
            return ApiResponse.error("移除失败");
        }
    }

    @ApiOperation(value = "addProjectUser",notes = "添加项目成员",httpMethod ="POST")
    @PostMapping("/addProjectUser")
    @Transactional
    public ApiResponse addProjectUser(@RequestBody List<ProjectUserDto> projectUserDtos){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        if (CollectionUtils.isEmpty(projectUserDtos)){
            return ApiResponse.error("成员列表为空");
        }
        List<ProjectUserRelationInfo> list=new ArrayList<>();
        List<ProjectUserRelationInfo> projectUserRelationInfos = iProjectUserRelationInfoService.selectProjectUserRelationInfoList(new ProjectUserRelationInfo(projectUserDtos.get(0).getProjectId()));
        for (ProjectUserDto projectUserDto:projectUserDtos){
            list.add(new ProjectUserRelationInfo(projectUserDto.getProjectId(),projectUserDto.getUserId()));
        }
        list.removeAll(projectUserRelationInfos);
        int count=iProjectUserRelationInfoService.insertProjectList(list);
//        redisUtil.delKey(String.format(PROJECT_USER_TREE, projectUserDtos.get(0).getProjectId()));
        return ApiResponse.success();
    }

    @ApiOperation(value = "deleteProject",notes = "删除项目",httpMethod ="POST")
    @PostMapping("/deleteProject")
    @Transactional
    public ApiResponse deleteProject(@RequestBody ProjectUserDTO projectUserDTO){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        try {
            int count=iProjectInfoService.deleteProject(projectUserDTO);
//            redisUtil.delKey(String.format(PROJECT_USER_TREE, projectUserDTO.getProjectId()));
        } catch (Exception e) {
            logger.error("业务处理异常", e);
            return ApiResponse.error("删除项目失败");
        }
        return ApiResponse.success();
    }



}
