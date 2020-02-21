package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.FeedBackDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenService tokenService;
    /**
     * 用户登陆
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "login",notes = "用户登陆",httpMethod ="POST")
    @PostMapping("/login")
    public ApiResponse login(UserDto userDto){
        return ApiResponse.success();
    }

    /**
     * 用户登出
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "logout",notes = "用户登出",httpMethod ="POST")
    @PostMapping("/logout")
    public ApiResponse logout(UserDto userDto){

        return null;
    }


    /**
     * 添加用户
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "register",notes = "添加用户",httpMethod ="POST")
    @PostMapping("/register")
    public ApiResponse register(UserDto userDto){

        return null;
    }

    /**
     * Excel 批量导入添加用户
     * @param  userDtos
     * @return
     */
    @ApiOperation(value = "registerByExcelImport",notes = "Excel 批量导入添加用户",httpMethod ="POST")
    @PostMapping("/registerByExcelImport")
    public ApiResponse registerByExcelImport(List<UserDto> userDtos){

        return null;
    }


    /**
     * 设置头像
     * @param  userDtos
     * @return
     */
    @ApiOperation(value = "setHeadImages",notes = "设置头像",httpMethod ="POST")
    @PostMapping("/setHeadImages")
    public ApiResponse setHeadImages(UserDto userDtos,File imageFile){


        return null;
    }



    /**
     * 获取用户信息
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getUserInfo",notes = "获取用户信息",httpMethod ="POST")
    @PostMapping("/getUserInfo")
    public ApiResponse getUserInfo(UserDto userDto){

        return null;
    }








}
