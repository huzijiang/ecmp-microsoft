package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.car.EmergencyContactDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.dto.DriverDTO;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Driver;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:17
 */
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private IDriverInfoService driverInfoService;

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



}
