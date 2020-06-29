package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.dto.OrderEvaluationDto;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: caobj
 * @Date: 2020-3-7
 */
@RestController
@Slf4j
@RequestMapping("/evaluation")
public class EvaluationController {


    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpUserFeedbackInfoService feedbackInfoService;
    @Autowired
    private IEcmpUserFeedbackImageService feedbackImageService;

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
            Long feedId=feedbackInfoService.saveOrderEvaluation(evaluationDto,loginUser.getUser().getUserId());
            return ApiResponse.success(feedId);
        }catch (Exception e){
            log.error("业务处理异常", e);
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

           List<EcmpUserFeedbackInfo> feedbackInfos = feedbackInfoService.selectEcmpUserFeedbackInfoList(new EcmpUserFeedbackInfo(evaluationDto.getOrderId()));
           if (CollectionUtils.isEmpty(feedbackInfos)){
               return  ApiResponse.success();
           }
           EcmpUserFeedbackInfo ecmpUserFeedbackInfo = feedbackInfos.get(0);
           BeanUtils.copyProperties(ecmpUserFeedbackInfo,evaluationDto);
           List<EcmpUserFeedbackImage> ecmpUserFeedbackImages = feedbackImageService.selectEcmpUserFeedbackImageList(new EcmpUserFeedbackImage(ecmpUserFeedbackInfo.getFeedbackId()));
           if (!CollectionUtils.isEmpty(ecmpUserFeedbackImages)){
               String imgs="";
               for (EcmpUserFeedbackImage image:ecmpUserFeedbackImages){
                   imgs+=","+image.getImageUrl();
               }
               if (StringUtils.isNotBlank(imgs)){
                   evaluationDto.setImgUrls(imgs.substring(1));
               }
           }
           return ApiResponse.success(evaluationDto);
        }catch (Exception e){
            log.error("业务处理异常", e);
            return  ApiResponse.error("获取行程异议异常!");
        }
    }


}
