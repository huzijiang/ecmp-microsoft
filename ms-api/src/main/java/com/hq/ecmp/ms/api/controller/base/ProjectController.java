package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.ProjectDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            return ApiResponse.error("未查询到项目对象");
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
}
