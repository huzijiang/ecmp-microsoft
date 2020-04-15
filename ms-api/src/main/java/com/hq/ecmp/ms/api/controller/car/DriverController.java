package com.hq.ecmp.ms.api.controller.car;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;
import com.hq.ecmp.mscore.dto.DriverLocationDTO;
import com.hq.ecmp.mscore.dto.DriverScheduleDTO;
import com.hq.ecmp.mscore.service.IDriverHeartbeatInfoService;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import com.hq.ecmp.mscore.vo.DriverDutySummaryVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.car.EmergencyContactDto;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.ReassignInfo;
import com.hq.ecmp.mscore.dto.DriverDTO;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;

import io.swagger.annotations.ApiOperation;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:17
 */
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private IDriverInfoService driverInfoService;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private IDriverWorkInfoService driverWorkInfoService;
    
    @Autowired
    private IOrderStateTraceInfoService orderStateTraceInfoService;

    @Autowired
    private IDriverHeartbeatInfoService driverHeartbeatInfoService;

    /**
     * 获取可调度的司机信息
     * 注意：
     *   此处 司机  受到 用车制度、驾驶能力 等多重因素制约
     * @param
     * @return
     */
    @ApiOperation(value = "getDispatchableDriver",notes = "获取可调度的司机信息",httpMethod ="POST")
    @PostMapping("/getDispatchableDriver")
    public ApiResponse<List<DriverInfo>> getDispatchableDriver(@RequestBody(required = false) DriverDTO driverDTO){
        //查询可用驾驶员信息（可根据电话，驾驶员姓名模糊查询）TODO 可用与否得与订单挂钩吧
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setDriverName(driverDTO.getDriverName());
        driverInfo.setMobile(driverDTO.getDriverPhone());
        List<DriverInfo> driverInfos = driverInfoService.selectDriverInfoList(driverInfo);
        if(CollectionUtils.isEmpty(driverInfos)){
            return ApiResponse.success("暂无可用驾驶员");
        }
        return ApiResponse.success(driverInfos);
    }

    /**
     * 获取 调派给自己的行程任务
     * @param  driverDto  司机信息
     * @return
     */
    @ApiOperation(value = "getDispatchedJourneyTask",notes = "获取 调派给自己(司机)的行程任务",httpMethod ="POST")
    @PostMapping("/getDispatchedJourneyTask")
    public ApiResponse<List<DriverInfo>> getDispatchedJourneyTask(DriverDto driverDto){

        return null;
    }


    /**
     * 司机设置紧急联系人
     * @param  driverDto 司机信息
     * @param  emergencyContactDto 司机信息
     * @return
     */
    @ApiOperation(value = "setEmergencyContact",notes = "司机设置紧急联系人",httpMethod ="POST")
    @PostMapping("/setEmergencyContact")
    public ApiResponse setEmergencyContact(DriverDto driverDto, EmergencyContactDto emergencyContactDto){

        return null;
    }
    
    
    /**
     *司机申请改派
     */
    @ApiOperation(value = "applyReassignment ",notes = "司机申请改派",httpMethod ="POST")
    @PostMapping("/applyReassignment")
    public ApiResponse applyReassignment(Long orderNo,String reason){
    	 HttpServletRequest request = ServletUtils.getRequest();
         LoginUser loginUser = tokenService.getLoginUser(request);
         Long userId = loginUser.getUser().getUserId();
         boolean applyReassignment = orderStateTraceInfoService.applyReassignment(userId, orderNo, reason);
        if(applyReassignment){
        	return ApiResponse.success();
        }
         return ApiResponse.error();
    }

    /**
     *查询改派记录
     */
    @ApiOperation(value = "reassignDetail ",notes = "查询改派记录",httpMethod ="POST")
    @PostMapping("/reassignDetail")
    public ApiResponse<List<ReassignInfo>> reassignDetail(Long orderNo){
         return ApiResponse.success(orderStateTraceInfoService.queryReassignDetail(orderNo));
    }

    /**
     * 查询司机当月排班日期对应的出勤情况列表
     * @param
     * @return
     */
    @Log(title = "司机排班管理:司机排班情况", businessType = BusinessType.OTHER)
    @ApiOperation(value = "loadScheduleInfo",notes = "加载司机排班/出勤信息",httpMethod ="POST")
    @PostMapping("/loadScheduleInfo")
    public ApiResponse<DriverDutyPlanVO> loadScheduleInfo(@RequestBody(required = false) String scheduleDate){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            //查询司机当月排班日期对应的出勤情况列表
            DriverDutyPlanVO result = driverWorkInfoService.selectDriverScheduleByMonth(scheduleDate,userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("加载司机当月排班日期对应的出勤情况列表失败");
        }
    }

    /**
     * 加载司机应该出勤/已出勤天数
     * @param
     * @return
     */
    @Log(title = "司机排班管理:司机出勤统计", businessType = BusinessType.OTHER)
    @ApiOperation(value = "loadDutySummary",notes = "加载司机应该出勤/已出勤天数",httpMethod ="POST")
    @PostMapping("/loadDutySummary")
    public ApiResponse<DriverDutySummaryVO> loadDutySummary(@RequestBody(required = false) DriverScheduleDTO driverScheduleDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            //查询司机当月排班/出勤天数
            DriverDutySummaryVO dutySummary = driverWorkInfoService.selectDriverDutySummary(driverScheduleDTO.getScheduleDate(),userId);
            return ApiResponse.success(dutySummary);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("加载司机出勤信息失败");
        }
    }

    /**
     * 记录司机心跳
     * @param
     * @return
     */
    @ApiOperation(value = "recordDriverLocation",notes = "记录司机心跳",httpMethod ="POST")
    @PostMapping("/recordDriverLocation")
    public ApiResponse recordDriverLocation(@RequestBody DriverLocationDTO driverLocationDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser==null){
            return ApiResponse.error("用户未登陆");
        }
        if(loginUser.getDriver()==null){
            return ApiResponse.success("当前用户不是司机");
        }
        Long dreiverId=loginUser.getDriver().getDriverId();
        try {
            //TODO 暂时直接插入数据库 二期可能还得记录乘客的心跳
            int i = driverHeartbeatInfoService.insertDriverHeartbeatInfo(new DriverHeartbeatInfo(dreiverId,driverLocationDTO.getOrderId(),new BigDecimal(driverLocationDTO.getLongitude()),new BigDecimal(driverLocationDTO.getLatitude())));
            if (i>0){
                return ApiResponse.success("司机位置记录成功");
            }else{
                return ApiResponse.error("记录司机心跳失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("记录司机心跳失败");
        }
    }




}
