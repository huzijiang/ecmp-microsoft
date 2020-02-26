package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private IProjectUserRelationInfoService iProjectUserRelationInfoService;

    /**
     * 查询用户 所在（子）公司的项目信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "getProjectsByUser",notes = "查询用户 所在（子）公司的项目信息 ",httpMethod ="POST")
    @PostMapping("/getProjectsByUser")
    public ApiResponse<List<ProjectInfo>> getProjectsByUser(UserDto userDto){

        //根据用户Id查询项目对象
        ProjectUserRelationInfo projectUserRelationInfo = iProjectUserRelationInfoService.selectProjectUserRelationInfoById(userDto.getUserId());
        //根据项目id查询项目信息
        ProjectInfo projectInfo = iProjectInfoService.selectProjectInfoById(projectUserRelationInfo.getProjectId());
        //根据项目对象查询项目信息列表
        List<ProjectInfo> projectInfoList = iProjectInfoService.selectProjectInfoList(projectInfo);
        return ApiResponse.success(projectInfoList);
    }
}
