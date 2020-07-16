package com.hq.ecmp.ms.api.controller.questionnaire;


import com.alibaba.fastjson.JSON;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DriverAppraiseDto;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.QuestionnaireVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/questionnaire")
@Slf4j
public class EcmpQuestionnaireController {
    @Autowired
    private IEcmpQuestionnaireService ecmpQuestionnaireService;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "submit",notes = "提交反馈",httpMethod = "POST")
    @PostMapping("/submit")
    public ApiResponse ranking(@RequestBody EcmpQuestionnaire ecmpQuestionnaire){
        try {
            //车牌号查询车辆id
            CarInfo carInfo = new CarInfo();
            carInfo.setCarLicense(ecmpQuestionnaire.getCarLicense());
            List<CarInfo> carInfos = carInfoMapper.selectCarInfoList(carInfo);
            if(carInfos.isEmpty()){
                return ApiResponse.error("车辆信息未录入");
            }
            ecmpQuestionnaire.setCarId(carInfos.get(0).getCarId());
            //查询车辆订单
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCarId(ecmpQuestionnaire.getCarId());
            List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoList(orderInfo);
            //时间倒叙，理论上最新的车辆相关订单就是当前评价的订单
            orderInfos.sort(Comparator.comparing(OrderInfo::getCreateTime).reversed());
            OrderInfo info = orderInfos.get(0);
//  奇怪的绑定订单操作，先注释掉，默认绑定车辆最新的订单  update_by huzj
//            for(OrderInfo x:orderInfos){
//                Map map = new HashMap();
//                map.put("orderId",x.getOrderId());
//                //查询行程
//                JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(x.getJourneyId());
//                map.put("begin",journeyInfo.getUseCarTime());
//                Calendar calendar  =   Calendar.getInstance();
//                calendar.setTime(journeyInfo.getUseCarTime());
//                //半日租
//                if(journeyInfo.getCharterCarType().equals("T001")){
//                    calendar.add(calendar.HOUR_OF_DAY,4);
//                    map.put("end",calendar.getTime());
//                }
//                //整日租
//                if(journeyInfo.getCharterCarType().equals("T002")){
//                    calendar.add(calendar.HOUR_OF_DAY,8);
//                    map.put("end",calendar.getTime());
//                }
//                //多日租
//                if(journeyInfo.getCharterCarType().equals("T009")){
//                    calendar.add(calendar.DATE, Integer.parseInt(journeyInfo.getUseTime()));//把日期往后增加n天.正数往后推,负数往前移动
//                    map.put("end",calendar.getTime());
//                }
//                if (ecmpQuestionnaire.getUseCarTime().after((Date)map.get("begin"))
//                        && ecmpQuestionnaire.getUseCarTime().before((Date)map.get("end"))
//                ){
//                    info = x;
//                }
//            };
            if(info==null){
                return ApiResponse.error("无匹配订单");
            }
            ecmpQuestionnaire.setOrderNumber(info.getOrderNumber());
            ecmpQuestionnaire.setDriverId(info.getDriverId());
            int temp = ecmpQuestionnaireService.insertEcmpQuestionnaire(ecmpQuestionnaire);
            if(temp>0){
                return ApiResponse.success("提交成功，感谢您的反馈");
            }else{
                return ApiResponse.error("提交失败");
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("系统错误");
        }
    }

    @ApiOperation(value = "dispatcherDriverList",notes = "查询调度员所在车队的司机列表",httpMethod = "POST")
    @PostMapping("/dispatcherDriverList")
    public ApiResponse dispatcherDriverList(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            List<DriverQueryResult> drivers = ecmpQuestionnaireService.dispatcherDriverList(loginUser);
            return ApiResponse.success("查询成功",drivers);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation(value = "dispatcherCarList",notes = "查询调度员所在车队的车辆列表",httpMethod = "POST")
    @PostMapping("/dispatcherCarList")
    public ApiResponse dispatcherCarList(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            List<CarInfo> cars = ecmpQuestionnaireService.dispatcherCarList(loginUser);
            return ApiResponse.success("查询成功",cars);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
    }


    @ApiOperation(value = "dispatcherDriverAppraiseList",notes = "查询调度员所在车队的司机评价列表",httpMethod = "POST")
    @PostMapping("/dispatcherDriverAppraiseList")
    public ApiResponse dispatcherDriverAppraiseList(@RequestBody DriverAppraiseDto driverAppraiseDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            PageResult<QuestionnaireVo> result = ecmpQuestionnaireService.dispatcherDriverAppraiseList(loginUser,driverAppraiseDto);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
    }


}
