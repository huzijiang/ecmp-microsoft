package com.hq.ecmp.ms.api.controller.driver;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 驾驶员信息
 * @author :shixin
 * @Date： 2020-3-20
 */
@RestController
@RequestMapping("/driverNews")
public class DriverNewsController {
    @Autowired
    private IDriverInfoService iDriverInfoService;
    @Autowired
    private IDriverCarRelationInfoService driverCarRelationInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IDriverWorkInfoService driverWorkInfoService;

    /**
     *驾驶员可用车辆列表
     * @param driverDTO
     * @return
     */
    @ApiOperation(value="getDriverCanUseCarsList" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/getDriverCanUseCarsList")
    public ApiResponse<List<DriverCanUseCarsDTO>> getDriverCanUseCarsList(@RequestBody DriverNewDTO driverDTO){
        List<DriverCanUseCarsDTO>  driverCanUseCarsList = iDriverInfoService.getDriverCanCar(driverDTO.getDriverId());
        if(CollectionUtils.isNotEmpty(driverCanUseCarsList)){
            return ApiResponse.success(driverCanUseCarsList);
        }else {
            return ApiResponse.error("未查询到驾驶员可用车辆");
        }
    }
    /**
     *驾驶员失效列表
     * @param driverDTO
     * @return
     */
    @ApiOperation(value="getDriverLoseList" ,notes="查询驾驶员失效列表", httpMethod = "POST")
    @PostMapping("/getDriverLoseList")
    public ApiResponse<List<DriverLoseDTO>> getDriverLoseList(@RequestBody DriverNewDTO driverDTO){
        List<DriverLoseDTO>  driverLoseList = iDriverInfoService.getDriverLoseList(driverDTO.getDeptId());
        if(CollectionUtils.isNotEmpty(driverLoseList)){
            return ApiResponse.success(driverLoseList);
        }else {
            return ApiResponse.error("未查询到失效驾驶员");
        }
    }
    /**
     *驾驶员失效数量
     * @param driverDTO
     * @return
     */
    @ApiOperation(value="getDriverLoseCount" ,notes="查询驾驶员失效数量", httpMethod = "POST")
    @PostMapping("/getDriverLoseCount")
    public ApiResponse  getDriverLoseCount(@RequestBody DriverNewDTO driverDTO){
        int driverCount = iDriverInfoService.getDriverLoseCount(driverDTO.getDeptId());
        if(driverCount >0){
            return ApiResponse.success(driverCount);
        }
        return ApiResponse.error("失效驾驶员数量为0");

    }
    /**
     *已失效驾驶员进行删除
     * @param driverDTO
     * @return
     */
    @ApiOperation(value="getDriverDelete" ,notes="删除已失效驾驶员", httpMethod = "POST")
    @PostMapping("/getDriverDelete")
    public ApiResponse  getDriverDelete(@RequestBody DriverNewDTO driverDTO){
        int deleteDriver = iDriverInfoService.deleteDriver(driverDTO.getDriverId());
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
     * @param driverNewDTO
     * @return
     */
    @ApiOperation(value="getDriverUpdateMobile" ,notes="修改驾驶员手机号", httpMethod = "POST")
    @PostMapping("/getDriverUpdateMobile")
    public ApiResponse  getDriverUpdateMobile(@RequestBody DriverNewDTO driverNewDTO){
        int updateDriverMob = iDriverInfoService.updateDriverMobile(driverNewDTO.getMobile(),driverNewDTO.getDriverId());
        if(updateDriverMob !=0){
            return ApiResponse.success("成功修改驾驶员手机号");
        }
        return ApiResponse.error("修改驾驶员手机号失败");
    }
    /**
     *设置驾驶员离职日期
     * @param driverNewDTO
     * @return
     */
    @ApiOperation(value="getDriverUpdateDimTime" ,notes="设置驾驶员离职日期", httpMethod = "POST")
    @PostMapping("/getDriverUpdateDimTime")
    public ApiResponse  getDriverUpdateDimTime(@RequestBody DriverNewDTO driverNewDTO){
        int updateDriverDimTime = iDriverInfoService.updateDriverDimTime(driverNewDTO.getDimTime(),driverNewDTO.getDriverId());
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
     * 查询司机当月排班日期对应的出勤情况列表
     * @param
     * @return
     */
    @ApiOperation(value = "getDriverScheduleInfo",notes = "加载司机排班/出勤信息",httpMethod ="POST")
    @PostMapping("/getDriverScheduleInfo")
    public ApiResponse<DriverDutyWorkVO> getDriverScheduleInfo(@RequestBody(required = false) String scheduleDate,
                                                          @RequestParam("driverId") Long driverId){
      //  HttpServletRequest request = ServletUtils.getRequest();
      //  LoginUser loginUser = tokenService.getLoginUser(request);
      //  Long userId = loginUser.getUser().getUserId();
        try {
            //查询司机当月排班日期对应的出勤情况列表
            DriverDutyWorkVO result = driverWorkInfoService.selectDriverSchedule(scheduleDate,driverId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("加载司机当月排班日期对应的出勤情况列表失败");
        }
    }
    /**
     * 驾驶员班次设置
     * @param
     * @return
     */
    @ApiOperation(value = "updateDriverWork",notes = "驾驶员班次设置",httpMethod ="POST")
    @PostMapping("/updateDriverWork")
    public ApiResponse updateDriverWork(@RequestBody List<DriverWorkDTO> DriverWorkList){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
         //   iDriverInfoService.bindDriverCars(DriverWorkDTO,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("新增车辆成功");
    }

    /**
     * 按月获取司机的排班详情
     * @return
     */
    @Log("按月获取司机的排班详情")
    @PostMapping("/getDriverMonthWorkDetail")
    public ApiResponse<List<DriverWorkInfoMonthVo>> getDriverMonthWorkDetail(@RequestParam("driverId")Long driverId,
                                                                       @RequestParam("month") String month){
        List<DriverWorkInfoMonthVo> driverWorkInfoMonthList = null;
        try {
            if(driverId == null || StringUtils.isBlank(month)){
                throw new Exception("参数异常");
            }
            driverWorkInfoMonthList = driverWorkInfoService.getDriverWorkInfoMonthList(driverId, month);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success(driverWorkInfoMonthList);
    }

    /**
     * 按月变更司机的排班
     * @param driverWorkInfoDetailVo
     * @return
     */
    @Log("按月变更司机的排班")
    @PostMapping("/updateDriverWorkDetailMonth")
    public ApiResponse updateDriverWorkDetailMonth(@RequestBody DriverWorkInfoDetailVo driverWorkInfoDetailVo){
        try {
            //获取登录用户
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            driverWorkInfoService.updateDriverWorkDetailMonth(driverWorkInfoDetailVo,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("司机排班变更失败");
        }
        return ApiResponse.success("司机排班变更成功");
    }
}
