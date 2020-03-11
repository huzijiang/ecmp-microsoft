package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.mscore.service.impl.EcmpUserFeedbackImageServiceImpl;
import com.hq.ecmp.util.FileUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/userfeedback")
public class UserFeedBackController {

    @Autowired
    IEcmpUserFeedbackInfoService ecmpUserFeedbackInfoService;
    @Autowired
    IEcmpUserFeedbackImageService ecmpUserFeedbackImageService;
    @Autowired
    TokenService tokenService;
    @Value("file.path.feedback")
    String feedbackPath;
    @Value("file.url.feedback")
    String feedbackUrl;
    /**
     * 用户意见反馈接口
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "saveFeedbackInfo",notes = "上传文件",httpMethod ="POST")
    @PostMapping(value = "/saveFeedbackInfo", headers = "content-type=multipart/form-data")
    public ApiResponse saveFile( @ApiParam(value = "图片",name = "files",required = true) MultipartFile[] files,String content,Long orderId,String type ) {
        Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        EcmpUserFeedbackInfo feedbackInfo = new EcmpUserFeedbackInfo();
        feedbackInfo.setContent(content);
        feedbackInfo.setOrderId(orderId);
        feedbackInfo.setType(type);
        feedbackInfo.setUserId(userId);
        feedbackInfo.setCreateTime(new Date());
        long feedbackId = ecmpUserFeedbackInfoService.insertEcmpUserFeedbackInfo(feedbackInfo);
        EcmpUserFeedbackImage feedbackImage = new EcmpUserFeedbackImage();
        feedbackImage.setFeedbackId(feedbackId);
        feedbackImage.setUserId(userId);
        for (int i = 0; i < files.length; i++) {
            String imgPath = FileUtils.uploadfile(files[i], File.separator + feedbackPath);
            feedbackImage.setImageUrl(feedbackUrl+imgPath);
            ecmpUserFeedbackImageService.insertEcmpUserFeedbackImage(feedbackImage);
        }
        return ApiResponse.success("上传成功");
    }






}
