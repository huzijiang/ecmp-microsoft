package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import io.swagger.annotations.ApiOperation;
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

    /**
     * 查询用户 所在（子）公司的项目信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "getProjectesByUser",notes = "查询用户 所在（子）公司的项目信息 ",httpMethod ="POST")
    @PostMapping("/getProjectesByUser")
    public ApiResponse<List<ProjectInfo>> getProjectesByUser(UserDto userDto){

        return null;
    }

}
