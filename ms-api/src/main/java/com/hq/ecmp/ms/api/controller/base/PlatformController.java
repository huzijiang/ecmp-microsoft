package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.AppCurrentVersionDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:02
 */
@RestController
@RequestMapping("/plat")
public class PlatformController {

    /**
     * 设置移动端当前版本
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "setAppCurrentVersion",notes = "设置移动端当前版本 ",httpMethod ="POST")
    @PostMapping("/setAppCurrentVersion")
    public ApiResponse setAppCurrentVersion(UserDto userDto, AppCurrentVersionDto appCurrentVersionDto){

        return null;
    }

    /**
     * 查询移动端当前版本
     * @return
     */
    @ApiOperation(value = "getAppCurrentVersion",notes = "查询移动端当前版本",httpMethod ="POST")
    @PostMapping("/getAppCurrentVersion")
    public ApiResponse getAppCurrentVersion(){

        return null;
    }



}
