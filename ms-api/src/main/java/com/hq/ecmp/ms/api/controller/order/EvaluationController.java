package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.ms.api.dto.order.OrderEvaluationDto;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.mscore.service.ZimgService;
import com.hq.ecmp.util.FileUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: caobj
 * @Date: 2020-3-7
 */
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {


    @Resource
    private TokenService tokenService;
    @Autowired
    private IEcmpUserFeedbackInfoService feedbackInfoService;
    @Autowired
    private IEcmpUserFeedbackImageService feedbackImageService;
    @Autowired
    private ZimgService zimgService;
    @Value("file.path.feedback")
    String feedbackPath;
    @Value("file.url.feedback")
    String feedbackUrl;

    /**
     *   @author caobj
     *   @Description 保存行程异议
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "保存行程异议",httpMethod = "POST")
    @RequestMapping("/saveOrderEvaluation")
    public ApiResponse<Long> saveOrderEvaluation(@RequestBody OrderEvaluationDto evaluationDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            EcmpUserFeedbackInfo ecmpUserFeedbackInfo = new EcmpUserFeedbackInfo();
            BeanUtils.copyProperties(evaluationDto,ecmpUserFeedbackInfo);
            ecmpUserFeedbackInfo.setUserId(loginUser.getUser().getUserId());
            int count = feedbackInfoService.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
            if (count>0){
                if (evaluationDto.getFiles()!=null&&evaluationDto.getFiles().size()>0){
                    EcmpUserFeedbackImage feedbackImage = new EcmpUserFeedbackImage();
                    feedbackImage.setFeedbackId(ecmpUserFeedbackInfo.getFeedbackId());
                    feedbackImage.setUserId(loginUser.getUser().getUserId());
                    for (int i = 0; i < evaluationDto.getFiles().size(); i++) {
                        String url = zimgService.uploadImage(evaluationDto.getFiles().get(i));
                        feedbackImage.setImageUrl(url);
                        feedbackImageService.insertEcmpUserFeedbackImage(feedbackImage);
                    }
                }
            }
            return ApiResponse.success(ecmpUserFeedbackInfo.getFeedbackId());
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("保存行程异议异常!");
        }
    }


    /**
     *   @author caobj
     *   @Description 获取行程异议详情
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "获取行程异议",httpMethod = "POST")
    @RequestMapping("/getOrderEvaluation")
    public ApiResponse<OrderEvaluationDto> getOrderEvaluation(@RequestBody OrderEvaluationDto evaluationDto){
        try {

           List<EcmpUserFeedbackInfo> ecmpUserFeedbackInfo = feedbackInfoService.selectEcmpUserFeedbackInfoList(new EcmpUserFeedbackInfo(evaluationDto.getOrderId()));
           if (CollectionUtils.isEmpty(ecmpUserFeedbackInfo)){
               return  ApiResponse.error("行程异议反馈不存在");
           }
           BeanUtils.copyProperties(ecmpUserFeedbackInfo,evaluationDto);
           List<EcmpUserFeedbackImage> ecmpUserFeedbackImages = feedbackImageService.selectEcmpUserFeedbackImageList(new EcmpUserFeedbackImage(ecmpUserFeedbackInfo.get(0).getFeedbackId()));
           if (!CollectionUtils.isEmpty(ecmpUserFeedbackImages)){
               List<String> imgs=new ArrayList<>();
               for (EcmpUserFeedbackImage image:ecmpUserFeedbackImages){
                   imgs.add(image.getImageUrl());
               }
               evaluationDto.setImgUrls(imgs);
           }
           return ApiResponse.success(evaluationDto);
        }catch (Exception e){
            e.printStackTrace();
            return  ApiResponse.error("获取行程异议异常!");
        }
    }


}
