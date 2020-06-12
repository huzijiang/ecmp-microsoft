package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.FeedBackTypeEnum;
import com.hq.ecmp.ms.api.dto.base.FeedBackDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfoVo;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackVo;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.HqAdmin;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TokenService tokenService;


    @Autowired
    private IEcmpOrgService iecmpOrgService;
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
     * 新增意见反馈
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
     * 获取意见反馈信息
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


    /**
     * 新增投诉建议
     * @param  feedBackDto
     * @return
     */
    @ApiOperation(value = "addFeedback",notes = "新增投诉建议",httpMethod ="POST")
    @PostMapping("/addFeedback")
    public ApiResponse addFeedback(@RequestBody FeedBackDto feedBackDto){

        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = new EcmpUserFeedbackInfo();
        ecmpUserFeedbackInfo.setType(FeedBackTypeEnum.COMPLAIN_TYPE.getType());//标注为投诉建议
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        int admin = HqAdmin.isAdmin(loginUser);
        ecmpUserFeedbackInfo.setIsAdmin(admin);
        ecmpUserFeedbackInfo.setUserId(loginUser.getUser().getUserId());
        ecmpUserFeedbackInfo.setContent(feedBackDto.getContent());
        ecmpUserFeedbackInfo.setTitle(feedBackDto.getTitle());
        int i = iEcmpUserFeedbackInfoService.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
        if (i == 1){
            return ApiResponse.success("新增投诉建议成功");
        }else {
            return ApiResponse.success("新增投诉建议失败");
        }
    }

    /**
     * 查询投诉建议
     * @param  feedBackDto
     * @return
     */
    @ApiOperation(value = "findFeedback",notes = "查询投诉建议",httpMethod ="POST")
    @PostMapping("/findFeedback")
    public ApiResponse<PageResult<EcmpUserFeedbackVo>> findFeedback(@RequestBody FeedBackDto feedBackDto){

        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = new EcmpUserFeedbackInfo();
        ecmpUserFeedbackInfo.setType(FeedBackTypeEnum.COMPLAIN_TYPE.getType());
        ecmpUserFeedbackInfo.setEcmpId(feedBackDto.getEcmpId());
        ecmpUserFeedbackInfo.setStatus(feedBackDto.getStatus());
        ecmpUserFeedbackInfo.setTitle(feedBackDto.getTitle());
        ecmpUserFeedbackInfo.setPageIndex(feedBackDto.getPageIndex());
        ecmpUserFeedbackInfo.setPageSize(feedBackDto.getPagesize());
        PageResult<EcmpUserFeedbackVo> info = iEcmpUserFeedbackInfoService.findFeedback(ecmpUserFeedbackInfo);
        if (null != info){
            return ApiResponse.success(info);
        }else {
            return ApiResponse.success("查询投诉建议失败");
        }
    }

    /**
     * 回复投诉
     * @param  feedBackDto
     * @return
     */
    @ApiOperation(value = "replyFeedback",notes = "回复投诉",httpMethod ="POST")
    @PostMapping("/replyFeedback")
    public ApiResponse updateFeedback(@RequestBody FeedBackDto feedBackDto){

        if(null != feedBackDto && StringUtils.isEmpty(feedBackDto.getResultContent())){
            return ApiResponse.error("回复投诉，缺失参数");
        }
        EcmpUserFeedbackInfoVo backInfo = new EcmpUserFeedbackInfoVo();
        backInfo.setResult(feedBackDto.getResultContent());
        backInfo.setFeedbackId(feedBackDto.getFeedId());
        int i = iEcmpUserFeedbackInfoService.updateFeedback(backInfo);
        if (i == 1){
            return ApiResponse.success("查询投诉建议成功");
        }else {
            return ApiResponse.success("查询投诉建议失败");
        }
    }

    /**
     * 回复投诉
     * @return
     */
    @ApiOperation(value = "getEcmpName",notes = "获取所有未删除的公司名称和id",httpMethod ="POST")
    @PostMapping("/getEcmpName")
    public ApiResponse getEcmpName(){

        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        List<Map> ecmpList = iecmpOrgService.getEcmpName(userId);
        if (null != ecmpList){
            return ApiResponse.success(ecmpList);
        }else {
            return ApiResponse.success("获取所有未删除的公司名称和id失败");
        }
    }

    /**
     * 获取所有未删除的公司名称和id  所有
     * @return
     */
    @ApiOperation(value = "getEcmpNameAll",notes = "获取所有未删除的公司名称和id",httpMethod ="POST")
    @PostMapping("/getEcmpNameAll")
    public ApiResponse getEcmpNameAll(){


        List<Map> ecmpList = iecmpOrgService.getEcmpNameAll();
        if (null != ecmpList){
            return ApiResponse.success(ecmpList);
        }else {
            return ApiResponse.success("获取所有未删除的公司名称和id失败");
        }
    }
}
