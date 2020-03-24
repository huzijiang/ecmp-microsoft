package com.hq.ecmp.ms.api.controller.driver;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.dto.CarDriverDTO;
import com.hq.ecmp.mscore.dto.DriverCanUseCarsDTO;
import com.hq.ecmp.mscore.dto.DriverCarDTO;
import com.hq.ecmp.mscore.dto.DriverLoseDTO;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 驾驶员信息
 * @author :shixin
 * @Date： 2020-3-20
 */
@RestController
@RequestMapping("/driverInfo")
public class DriverNewsController {
    @Autowired
    private IDriverInfoService iDriverInfoService;
    @Autowired
    private IDriverCarRelationInfoService driverCarRelationInfoService;
    @Autowired
    private TokenService tokenService;
    /**
     *驾驶员可用车辆列表
     * @param driverId
     * @return
     */
    @ApiOperation(value="getDriverCanUseCarsList" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/getDriverCanUseCarsList")
    public ApiResponse<List<DriverCanUseCarsDTO>> getDriverCanUseCarsList(Long driverId){
        List<DriverCanUseCarsDTO>  driverCanUseCarsList = iDriverInfoService.getDriverCanCar(driverId);
        if(CollectionUtils.isNotEmpty(driverCanUseCarsList)){
            return ApiResponse.success(driverCanUseCarsList);
        }else {
            return ApiResponse.error("未查询到驾驶员可用车辆");
        }
    }
    /**
     *驾驶员失效列表
     * @param deptId
     * @return
     */
    @ApiOperation(value="getDriverLoseList" ,notes="查询驾驶员失效列表", httpMethod = "POST")
    @PostMapping("/getDriverLoseList")
    public ApiResponse<List<DriverLoseDTO>> getDriverLoseList(Long deptId){
        List<DriverLoseDTO>  driverLoseList = iDriverInfoService.getDriverLoseList(deptId);
        if(CollectionUtils.isNotEmpty(driverLoseList)){
            return ApiResponse.success(driverLoseList);
        }else {
            return ApiResponse.error("未查询到失效驾驶员");
        }
    }
    /**
     *驾驶员失效数量
     * @param deptId
     * @return
     */
    @ApiOperation(value="getDriverLoseCount" ,notes="查询驾驶员失效数量", httpMethod = "POST")
    @PostMapping("/getDriverLoseCount")
    public ApiResponse  getDriverLoseCount(Long deptId){
        int driverCount = iDriverInfoService.getDriverLoseCount(deptId);
        if(driverCount >0){
            return ApiResponse.success(driverCount);
        }
        return ApiResponse.error("失效驾驶员数量为0");

    }
    /**
     *已失效驾驶员进行删除
     * @param driverId
     * @return
     */
    @ApiOperation(value="getDriverDelete" ,notes="删除已失效驾驶员", httpMethod = "POST")
    @PostMapping("/getDriverDelete")
    public ApiResponse  getDriverDelete(Long driverId){
        int deleteDriver = iDriverInfoService.deleteDriver(driverId);
        if(deleteDriver !=0){
            return ApiResponse.success("删除已失效驾驶员成功");
        }
        return ApiResponse.error("删除以失效驾驶员失败");
    }

    /**
     *修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    @ApiOperation(value="getDriverUpdate" ,notes="修改驾驶员", httpMethod = "POST")
    @PostMapping("/getDriverUpdate")
    public ApiResponse  getDriverUpdate(DriverCreateInfo driverCreateInfo){
        int updateDriver = iDriverInfoService.updateDriver(driverCreateInfo);
        if(updateDriver !=0){
            return ApiResponse.success("修改驾驶员成功");
        }
        return ApiResponse.error("修改驾驶员失败");
    }
    /**
     *修改驾驶员手机号
     * @param mobile
     * @return
     */
    @ApiOperation(value="getDriverUpdateMobile" ,notes="修改驾驶员手机号", httpMethod = "POST")
    @PostMapping("/getDriverUpdateMobile")
    public ApiResponse  getDriverUpdateMobile(String mobile){
        int updateDriverMob = iDriverInfoService.updateDriverMobile(mobile);
        if(updateDriverMob !=0){
            return ApiResponse.success("成功修改驾驶员手机号");
        }
        return ApiResponse.error("修改驾驶员手机号失败");
    }
    /**
     *设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    @ApiOperation(value="getDriverUpdateDimTime" ,notes="修改驾驶员离职日期", httpMethod = "POST")
    @PostMapping("/getDriverUpdateDimTime")
    public ApiResponse  getDriverUpdateDimTime(String dimTime){
        int updateDriverDimTime = iDriverInfoService.updateDriverDimTime(dimTime);
        if(updateDriverDimTime !=0){
            return ApiResponse.success("成功修改驾驶员离职日期");
        }
        return ApiResponse.error("修改驾驶员离职日期失败");
    }

    /**
     * 解绑驾驶员车辆
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "removeDriversCar",notes = "解绑驾驶员车辆",httpMethod ="POST")
    @PostMapping("/removeDriversCar")
    public ApiResponse removeDriversCar(@RequestBody CarDto carDto){
        try {
            driverCarRelationInfoService.removeCarDriver(carDto.getCarId(),carDto.getUserId(),carDto.getDriverId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("解绑成功");
    }
    /**
     * 新增车辆：驾驶员绑定车辆
     * @param
     * @return
     */
    @ApiOperation(value = "bindDriverCars",notes = "驾驶员新增车辆",httpMethod ="POST")
    @PostMapping("/bindDriverCars")
    public ApiResponse bindDriverCars(@RequestBody DriverCarDTO driverCarDTO){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            iDriverInfoService.bindDriverCars(driverCarDTO,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("新增车辆成功");
    }
    /**
     * 驾驶员班次设置
     * @param
     * @return
     */

}
