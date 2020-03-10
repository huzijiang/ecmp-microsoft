package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Driver;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private IEnterpriseCarTypeInfoService enterpriseCarTypeInfoService;
    @Autowired
    private TokenService tokenService;

    /**
     * 获取可调度的车辆信息
     * 注意：
     *   此处 车辆  受到 用车制度 等多重因素制约
     * @param  orderDto  订单信息
     * @return
     */
    @ApiOperation(value = "getDispatcheableCar",notes = "获取可调度的车辆信息",httpMethod ="POST")
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
    @ApiOperation(value = "reportCarLocation",notes = "上报车辆位置",httpMethod ="POST")
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

    /**
     * 查询查询用户企业有效车型 豪华型 公务型
     * @return
     */
    @ApiOperation(value = "getAllEffectiveCarTypes",notes = "查询用户公司有效自有公车车型 公务型，豪华型",httpMethod ="POST")
    @PostMapping("/getAllEffectiveCarTypes")
    public ApiResponse<List<EnterpriseCarTypeInfo>> getEffectiveCarTypes(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        List<EnterpriseCarTypeInfo> list = null;
        try {
            list = enterpriseCarTypeInfoService.selectEffectiveCarTypes(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
        if(CollectionUtils.isEmpty(list)){
            return ApiResponse.error("暂无数据");
        }
        return ApiResponse.success(list);
    }


}
