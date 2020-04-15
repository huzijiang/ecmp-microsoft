package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.FeedBackDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private IEcmpUserService iEcmpUserService;

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

        EcmpUser ecmpUser = iEcmpUserService.selectEcmpUserById(userDto.getUserId());
        int i = iEcmpUserService.insertEcmpUser(ecmpUser);
        if (i == 1){
            return ApiResponse.success("添加用户成功");
        }else {
            return ApiResponse.error("添加用户失败");
        }
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
    
    
    
    /**
     * 获取用户是否调度员
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "isDispatcher",notes = "获取用户是否是调度员",httpMethod ="POST")
    @PostMapping("/isDispatcher")
    public ApiResponse getIsDispatcher(){
    	 HttpServletRequest request = ServletUtils.getRequest();
	        LoginUser loginUser = tokenService.getLoginUser(request);
    	boolean dispatcher = iEcmpUserService.isDispatcher(loginUser.getUser().getUserId());
        return ApiResponse.success(dispatcher);
    }



    


}
