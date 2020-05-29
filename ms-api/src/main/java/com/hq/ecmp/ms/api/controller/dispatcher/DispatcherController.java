package com.hq.ecmp.ms.api.controller.dispatcher;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.enumerate.DispatchStrategyEnum;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.domain.ApplyDispatch;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.CostConfigInfoMapper;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategyEngineFactory;
import com.hq.ecmp.mscore.vo.DispatchVo;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description TODO 佛山包车业务调度模块
 * @Author yj
 * @Date 2020/5/24 10:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/dispatch/charterCar")
public class DispatcherController {

    @Resource
    private OrderInfoTwoService orderInfoTwoService;
    @Resource
    private TokenService tokenService;
    @Resource
    private CostConfigInfoMapper costConfigInfoMapper;
    @Resource
    private DispatchStrategyEngineFactory dispatchStrategyEngineFactory;

    /**
     * 获取调度列表数据
     */
    @PostMapping("/getDispatcherList")
    @ApiOperation(value = "佛山调度列表")
    @Log(value = "佛山调度列表")
    @com.hq.core.aspectj.lang.annotation.Log(title = "佛山调度列表",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    public ApiResponse<Map<String, Object>>  getDispatcherList(@RequestBody ApplyDispatch query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            Map<String, Object> list = orderInfoTwoService.queryDispatchListCharterCar(query, loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }

    /**
     * 获取首页调度列表数据
     */
    @PostMapping("/queryHomePageDispatchList")
    public ApiResponse<PageResult<DispatchVo>>  queryHomePageDispatchList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<DispatchVo> list = orderInfoTwoService.queryHomePageDispatchListCharterCar(query,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }

    /**
     * 佛山调度可用外部车队列表
     */
    @PostMapping("/dispatcherCarGroupList")
    @ApiOperation(value = "佛山调度可用外部车队列表")
    @Log(value = "佛山调度可用外部车队列表")
    @com.hq.core.aspectj.lang.annotation.Log(title = "佛山调度可用外部车队列表",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    public ApiResponse<List<CarGroupInfo>> dispatcherCarGroupList(Long orderId){
        List<CarGroupInfo> carGroupInfos = new ArrayList<>();
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            carGroupInfos = orderInfoTwoService.dispatcherCarGroupList(orderId, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取车队列表失败");
        }
        return  ApiResponse.success(carGroupInfos);
    }

    /**
     * 佛山内外调度派车接口
     * @return
     */
    @PostMapping("/dispatcherSendCar")
    @ApiOperation(value = "佛山内外调度派车接口")
    @Log(value = "佛山内外调度派车接口")
    @com.hq.core.aspectj.lang.annotation.Log(title = "佛山内外调度派车接口",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    public ApiResponse dispatcherSendCar(@RequestBody DispatchSendCarDto dispatchSendCarDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            dispatchSendCarDto.setUserId(loginUser.getUser().getUserId());
            if(dispatchSendCarDto.getUseCarGroupType().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_CAR_DRIVER)){
                        dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.InCarAndDriverStrategy.getStrategyServiceName())
                                .dispatch(dispatchSendCarDto);
                }else if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_CAR)){
                    if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_YES)){
                        dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.InCarAndSelfDriverStrategy.getStrategyServiceName())
                                .dispatch(dispatchSendCarDto);
                    }else if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_NO)){
                        dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.InCarAndOutDriverStrategy.getStrategyServiceName())
                                .dispatch(dispatchSendCarDto);
                    }

                }else if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_DRIVER)){
                    dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.InDriverAndOutCarStrategy.getStrategyServiceName())
                            .dispatch(dispatchSendCarDto);
                }
            }else if(dispatchSendCarDto.getUseCarGroupType().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_OUT)){
                if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_YES)){
                    dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.OutCarAndSelfDriverStrategy.getStrategyServiceName())
                            .dispatch(dispatchSendCarDto);
                }else if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_NO)){
                    dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.OutCarAndOutDriverStrategy.getStrategyServiceName())
                            .dispatch(dispatchSendCarDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("派车失败");
        }
        return ApiResponse.success("派车成功");
    }


    /**
     * 调度员驳回
     */
    @PostMapping("/dismissedDispatch")
    public ApiResponse  dismissedDispatch(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            orderInfoTwoService.dismissedDispatch(query,loginUser);
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("调度驳回失败");
        }
    }


    /**
     * 获取当前调度员的待派车，已派车，已过期数量
     * @param
     * @return
     */
    @ApiOperation(value = "getOrderSituationDispatchCount",notes = "获取当前调度员的待派车，已派车，已过期数量",httpMethod ="POST")
    @com.hq.core.aspectj.lang.annotation.Log(title = "用车申请", content = "获取当前调度员的待派车，已派车，已过期数量",businessType = BusinessType.OTHER)
    @PostMapping("/getOrderSituationDispatchCount")
    public JSONObject getUseApplyCounts(@RequestBody ApplyDispatchQuery applyDispatchQuery){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        JSONObject jsonObject = new JSONObject();
        try {
            applyDispatchQuery.setHomePageWaitingCarState("S100");
            List<DispatchVo> waitingCarList = orderInfoTwoService.queryDispatchListCharterCars(applyDispatchQuery,loginUser);
            jsonObject.put("waitingCarCount",waitingCarList.size());
            applyDispatchQuery.setHomePageUsingCarState("S299");
            applyDispatchQuery.setHomePageWaitingCarState("");
            List<DispatchVo> usingCarList = orderInfoTwoService.queryDispatchListCharterCars(applyDispatchQuery,loginUser);
            jsonObject.put("usingCarCount",usingCarList.size());
            applyDispatchQuery.setHomePageExpireCarState("S900");
            applyDispatchQuery.setHomePageUsingCarState("");
            applyDispatchQuery.setHomePageWaitingCarState("");
            List<DispatchVo> expireCarList = orderInfoTwoService.queryDispatchListCharterCars(applyDispatchQuery,loginUser);
            jsonObject.put("expireCarCount",expireCarList.size());
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
        }
        return jsonObject;
    }

}
