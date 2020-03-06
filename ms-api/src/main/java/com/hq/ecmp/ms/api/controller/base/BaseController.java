package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.FeedBackDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    @Autowired
    private IEcmpUserFeedbackImageService ecmpUserFeedbackImageService;

    @Autowired
    private IEcmpUserFeedbackInfoService iEcmpUserFeedbackInfoService;

    /**
     * 获取短信验证码
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getSmsVerifyCode",notes = "获取短信验证码",httpMethod ="POST")
    @PostMapping("/getSmsVerifyCode")
    public ApiResponse<EcmpNotice> getSmsVerifyCode(UserDto userDto){

        return null;
    }


    /**
     * 查询当前天气
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getWeather",notes = "查询当前天气",httpMethod ="POST")
    @PostMapping("/getWeather")
    public ApiResponse<EcmpNotice> getWeather(UserDto userDto){

        return null;
    }


    /**
     * 意见反馈
     * @param  feedBackDto
     * @return
     */
    @ApiOperation(value = "createFeedback",notes = "新增意见反馈",httpMethod ="POST")
    @PostMapping("/createFeedback")
    public ApiResponse createFeedback(FeedBackDto feedBackDto){

        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = iEcmpUserFeedbackInfoService.selectEcmpUserFeedbackInfoById(feedBackDto.getUserId());
        int i = iEcmpUserFeedbackInfoService.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
        if (i == 1){
            return ApiResponse.success("创建意见反馈成功");
        }else {
            return ApiResponse.success("创建意见反馈失败");
        }
    }

    /**
     * 意见反馈
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getFeedbackInfo",notes = "获取意见反馈信息",httpMethod ="POST")
    @PostMapping("/getFeedbackInfo")
    public ApiResponse<EcmpUserFeedbackInfo> getFeedbackInfo(UserDto userDto){

        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = iEcmpUserFeedbackInfoService.selectEcmpUserFeedbackInfoById(userDto.getUserId());
        if (ObjectUtils.isNotEmpty(ecmpUserFeedbackInfo)){
            return ApiResponse.success(ecmpUserFeedbackInfo);
        }else {
            return ApiResponse.error("获取意见反馈信息异常");
        }
    }
}
