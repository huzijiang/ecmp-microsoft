package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 11:59
 */
@RestController
@RequestMapping("/regime")
public class RegimeController {

    @Autowired
    private IRegimeInfoService regimeInfoService;


    /**
     * 根据用户信息查询用户的用车制度信息
     * @param userDto userDto
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getUserRegimes",notes = "根据用户信息查询用户的用车制度信息",httpMethod ="GET")
    @PostMapping("/getUserRegimes")
    public ApiResponse<List<RegimeInfo>> getUserRegimes(UserDto userDto){

        return null;
    }

    /**
     *
     * 查询所有的用车制度信息
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getAllRegimes",notes = "查询所有的用车制度信息",httpMethod ="GET")
    @PostMapping("/getAllRegimes")
    public ApiResponse<List<RegimeInfo>> getAllRegimes(){

        return null;
    }

    /**
     * 通过用车制度编号,查询用车制度的详细信息
     * @param regimeDto regimeDto
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getRegimeInfo",notes = "通过用车制度编号,查询用车制度的详细信息",httpMethod ="GET")
    @PostMapping("/getRegimeInfo")
    public ApiResponse<RegimeInfo> getRegimeInfo(RegimeDto regimeDto){

        return null;
    }



}
