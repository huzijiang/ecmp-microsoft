package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.car.EmergencyContactDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Driver;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:17
 */
@RestController
@RequestMapping("/driver")
public class DriverController {


    /**
     * 获取可调度的司机信息
     * 注意：
     *   此处 司机  受到 用车制度、驾驶能力 等多重因素制约
     * @param  orderDto  订单信息
     * @return
     */
    @ApiOperation(value = "getDispatchableDriver",notes = "获取可调度的司机信息",httpMethod ="POST")
    @PostMapping("/getDispatchableDriver")
    public ApiResponse<List<DriverInfo>> getDispatchableDriver(OrderDto orderDto){

        return null;
    }

    /**
     * 获取 调派给自己的行程任务
     * @param  driverDto  司机信息
     * @return
     */
    @ApiOperation(value = "getDispatchedJourneyTask",notes = "获取可调度的司机信息",httpMethod ="POST")
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
    @ApiOperation(value = "getUserInfo",notes = "司机设置紧急联系人",httpMethod ="POST")
    @PostMapping("/getUserInfo")
    public ApiResponse setEmergencyContact(DriverDto driverDto, EmergencyContactDto emergencyContactDto){

        return null;
    }



}
