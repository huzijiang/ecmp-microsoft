package com.hq.ecmp.ms.api.controller.safe;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.UserEmergencyContactInfo;
import com.hq.ecmp.mscore.service.UserEmergencyContactInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户紧急联系人信息表(UserEmergencyContactInfo)表控制层
 *
 * @author makejava
 * @since 2020-03-10 00:04:51
 */
@RestController
@RequestMapping("/userEmergencyContactInfo")
@Api(description = "用户紧急联系人")
public class UserEmergencyContactInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private UserEmergencyContactInfoService userEmergencyContactInfoService;

    @Autowired
    private TokenService tokenService;
    /**
     * 新增紧急联系人
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "addUserEmergency",notes = "新增紧急联系人",httpMethod ="POST")
    @PostMapping("/addUserEmergency")
    public ApiResponse addUserEmergency(String name, String mobile){
        Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        UserEmergencyContactInfo userEmergency = UserEmergencyContactInfo.builder()
                .name(name)
                .mobile(mobile)
                .userId(userId)
                .createBy(userId)
                .createTime(new Date())
                .build();
        userEmergencyContactInfoService.insert(userEmergency);
        return ApiResponse.success();
    }

    /**
     * 修改联系人
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "updateUserEmergency",notes = "修改联系人",httpMethod ="POST")
    @PostMapping("/updateUserEmergency")
    public ApiResponse updateUserEmergency(long userEmergencyId,String newName,String newMobile){
        Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        UserEmergencyContactInfo userEmergency = UserEmergencyContactInfo.builder()
                .id(userEmergencyId)
                .name(newName)
                .mobile(newMobile)
                .updateBy(userId)
                .updateTime(new Date())
                .build();
        userEmergencyContactInfoService.update(userEmergency);
        return ApiResponse.success();
    }

    /**
     * 删除联系人
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "addUserEmergency",notes = "修改联系人",httpMethod ="POST")
    @PostMapping("/deleteUserEmergency")
    public ApiResponse deleteUserEmergency(long userEmergencyId){
        UserEmergencyContactInfo userEmergency = UserEmergencyContactInfo.builder()
                .id(userEmergencyId)
                .build();
        userEmergencyContactInfoService.deleteById(userEmergencyId);
        return ApiResponse.success();
    }

    /**
     * 查询单个联系人
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "queryUserEmergency",notes = "查询单个联系人",httpMethod ="POST")
    @PostMapping("/queryUserEmergency")
    public ApiResponse<UserEmergencyContactInfo> queryUserEmergency(long userEmergencyId){
        UserEmergencyContactInfo userEmergencyContactInfo = userEmergencyContactInfoService.queryById(userEmergencyId);
        return ApiResponse.success(userEmergencyContactInfo);
    }

    /**
     * 查询联系人列表
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "queryUserEmergencyList",notes = "查询单个联系人",httpMethod ="POST")
    @PostMapping("/queryUserEmergencyList")
    public ApiResponse<List<UserEmergencyContactInfo>> queryUserEmergency(){
        Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        List<UserEmergencyContactInfo> list =  userEmergencyContactInfoService.queryByUserId(userId);
        return ApiResponse.success(list);
    }
}