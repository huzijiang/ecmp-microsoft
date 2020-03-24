package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.ms.api.dto.order.OrderEvaluationDto;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.ApiOperation;
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
import java.util.Date;
import java.util.List;

/**
 * @Author: caobj
 * @Date: 2020-3-7
 */
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {


    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpUserFeedbackInfoService feedbackInfoService;
    @Autowired
    private IEcmpUserFeedbackImageService feedbackImageService;
    @Autowired
    private IOrderStateTraceInfoService orderStateTraceInfoService;
    @Autowired
    @Lazy
    private IOrderInfoService orderInfoService;
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
                if (StringUtils.isNotBlank(evaluationDto.getImgUrls())){
                    EcmpUserFeedbackImage feedbackImage = new EcmpUserFeedbackImage();
                    feedbackImage.setFeedbackId(ecmpUserFeedbackInfo.getFeedbackId());
                    feedbackImage.setUserId(loginUser.getUser().getUserId());
                    String[] split = evaluationDto.getImgUrls().split(",");
                    for (String url:split) {
                        feedbackImage.setImageUrl(url);
                        feedbackImageService.insertEcmpUserFeedbackImage(feedbackImage);
                    }
                }
            }
            OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
            stateTraceInfo.setOrderId(evaluationDto.getOrderId());
            stateTraceInfo.setContent(evaluationDto.getContent());
            stateTraceInfo.setCreateBy(String.valueOf(loginUser.getUser().getUserId()));
            stateTraceInfo.setCreateTime(new Date());
            stateTraceInfo.setState(OrderStateTrace.OBJECTION.getState());
            orderStateTraceInfoService.insertOrderStateTraceInfo(stateTraceInfo);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(evaluationDto.getOrderId());
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            orderInfo.setUpdateTime(new Date());
            orderInfoService.updateOrderInfo(orderInfo);
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

           List<EcmpUserFeedbackInfo> feedbackInfos = feedbackInfoService.selectEcmpUserFeedbackInfoList(new EcmpUserFeedbackInfo(evaluationDto.getOrderId()));
           if (CollectionUtils.isEmpty(feedbackInfos)){
               return  ApiResponse.error("行程异议反馈不存在");
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
            e.printStackTrace();
            return  ApiResponse.error("获取行程异议异常!");
        }
    }


}
