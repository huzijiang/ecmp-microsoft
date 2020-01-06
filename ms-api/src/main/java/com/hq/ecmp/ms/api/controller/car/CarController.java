package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Driver;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@RequestMapping("/car")
public class CarController {

    /**
     * 获取可调度的车辆信息
     * 注意：
     *   此处 车辆  受到 用车制度 等多重因素制约
     * @param  orderDto  订单信息
     * @return
     */
    @ApiOperation(value = "getDispatchableCar",notes = "获取可调度的车辆信息",httpMethod ="POST")
    @PostMapping("/getDispatchableCar")
    public ApiResponse<List<CarInfo>> getDispatchableCar(OrderDto orderDto){

        return null;
    }

    /**
     * 确认取车-车辆出库-车钥匙交付算起
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "carGoOut",notes = " 确认取车-车辆出库-车钥匙交付算起",httpMethod ="POST")
    @PostMapping("/carGoOut")
    public ApiResponse<List<CarInfo>> carGoOut(CarDto carDto,UserDto userDto, DriverDto driverDto){

        return null;
    }


    /**
     * 确认还车-车辆回到公司车库
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "carBackHome",notes = "确认还车-车辆回到公司车库",httpMethod ="POST")
    @PostMapping("/carBackHome")
    public ApiResponse<List<CarInfo>> carBackHome(CarDto carDto,UserDto userDto, DriverDto driverDto){

        return null;
    }


    /**
     * 上报车辆位置
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "reportCarLocation",notes = "确认还车-车辆回到公司车库",httpMethod ="POST")
    @PostMapping("/reportCarLocation")
    public ApiResponse<List<CarInfo>> reportCarLocation(CarDto carDto){

        return null;
    }

    /**
     * 上报车辆实时信息
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "reportCarCurrentInfo",notes = "上报车辆实时信息",httpMethod ="POST")
    @PostMapping("/reportCarCurrentInfo")
    public ApiResponse<List<CarInfo>> reportCarCurrentInfo(CarDto carDto){

        return null;
    }


}
