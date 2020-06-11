package com.hq.ecmp.ms.api.controller.questionnaire;


import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.EcmpQuestionnaire;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.dto.DriverAppraiseDto;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.QuestionnaireVo;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/questionnaire")
public class EcmpQuestionnaireController {
    @Autowired
    private IEcmpQuestionnaireService ecmpQuestionnaireService;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "submit",notes = "提交反馈",httpMethod = "POST")
    @PostMapping("/submit")
    public ApiResponse ranking(@RequestBody EcmpQuestionnaire ecmpQuestionnaire){
        try {
            LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
            if(user.getUser()!=null){
                ecmpQuestionnaire.setCreateBy(user.getUser().getUserId().toString());
            }
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
            /*orderInfos.stream().forEach(x->{
                Map map = new HashMap();
                map.put("orderId",x.getOrderId());
                //查询行程
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                orderStateTraceInfo.setOrderId(x.getOrderId());
                List<OrderStateTraceInfo> orderStateTraceInfos = orderStateTraceInfoMapper.selectOrderStateTraceInfoList(orderStateTraceInfo);
                orderStateTraceInfos.stream().forEach(y->{
                    //上车时间
                    if(OrderStateTrace.SERVICE.getState().equals(y.getState())){
                        map.put("begin",y.getCreateTime());
                    }
                    //服务结束时间
                    if(OrderStateTrace.SERVICEOVER.getState().equals(y.getState())){
                        map.put("end",y.getCreateTime());
                    }
                });
                if(map.containsKey("end")){

                }
            });*/
            ecmpQuestionnaire.setDriverId(orderInfos.get(0).getDriverId());
            int temp = ecmpQuestionnaireService.insertEcmpQuestionnaire(ecmpQuestionnaire);
            if(temp>0){
                return ApiResponse.success("提交成功，感谢您的反馈");
            }else{
                return ApiResponse.error("提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }


}
