package com.hq.ecmp.ms.api.controller.base;

import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.ProjectDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.dto.Page;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.ProjectInfoDTO;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IProjectUserRelationInfoService iProjectUserRelationInfoService;

    /**
     * 查询用户 所在（子）公司的项目信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "getProjectsByUser",notes = "查询用户 所在（子）公司的项目信息 ",httpMethod ="POST")
    @PostMapping("/getProjectsByUser")
    public ApiResponse<List<ProjectInfo>> getProjectsByUser(UserDto userDto){
        //根据用户Id查询用户和项目关联关系对象
        ProjectUserRelationInfo projectUserRelationInfo = iProjectUserRelationInfoService.selectProjectUserRelationInfoById(userDto.getUserId());
        //根据项目id查询项目对象
        ProjectInfo projectInfo = iProjectInfoService.selectProjectInfoById(projectUserRelationInfo.getProjectId());
        //根据项目对象查询项目信息列表
        List<ProjectInfo> projectInfoList = iProjectInfoService.selectProjectInfoList(projectInfo);
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
        List<ProjectInfo> projectInfos = iProjectInfoService.getListByUserId(loginUser.getUser().getUserId(),projectDto.getProjectName());
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
        List<ProjectInfo> projectInfos = iProjectInfoService.selectProjectInfoList(new ProjectInfo(projectInfoDto.getProjectCode()));
        if (CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.error("该编号已存在,不可重复录入!");
        }
        return ApiResponse.success();
    }

    /**
     * 获取项目主管列表
     * @return
     */
    @ApiOperation(value = "getProjectDirector",notes = "获取项目主管列表",httpMethod ="POST")
    @PostMapping("/getProjectDirector")
    public ApiResponse getProjectDirector(@RequestBody ProjectInfoDTO projectInfoDto){
        return ApiResponse.success();
    }

    /**
     * 获取项目列表
     * @return
     */
    @ApiOperation(value = "getProjectList",notes = "获取项目列表",httpMethod ="POST")
    @PostMapping("/getProjectList")
    public ApiResponse<PageInfo<ProjectInfoVO>> getProjectList(@RequestBody PageRequest page){
        PageInfo<ProjectInfoVO> pageInfo= iProjectInfoService.getProjectList(page.getPageNum(),page.getPageSize(),page.getFatherProjectId());
        return ApiResponse.success(pageInfo);
    }

    /**
     * 新增项目
     * @return
     */
    @ApiOperation(value = "createProject",notes = "新增项目",httpMethod ="POST")
    @PostMapping("/createProject")
    public ApiResponse createProject(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(projectInfoDto,projectInfo);
        int i = iProjectInfoService.insertProjectInfo(projectInfo);
        if (i>0){
            if (projectInfoDto.getIsAllUserUse()==1){//全部员工
                this.saveUserProject(projectInfo.getProjectId());
            }
        }
        return ApiResponse.error("新建项目成功");
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
        int i = iProjectInfoService.updateProjectInfo(projectInfo);
        if (i>0){
            if (isAlluser!=projectInfoDto.getIsAllUserUse()){
                if (projectInfoDto.getIsAllUserUse()==1){//全部员工
                    saveUserProject(projectInfoDto.getProjectId());
                }else{
                    iProjectUserRelationInfoService.deleteProjectUserRelationInfoById(projectInfoDto.getProjectId());
                }
            }
        }
        return ApiResponse.error("新建项目成功");
    }

    private void saveUserProject(Long projectId){
        List<EcmpUser> ecmpUsers = ecmpUserService.selectEcmpUserList(null);
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


}
