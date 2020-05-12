package com.hq.ecmp.ms.api.controller.driver;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
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
    @Autowired
    IDriverInfoService driverInfoService;

    /**
     *驾驶员可用车辆列表
     * @param pageRequest
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "查询驾驶员可用车辆列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="getDriverCanUseCarsList" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/getDriverCanUseCarsList")
    public ApiResponse<PageResult<DriverCanUseCarsDTO>> getDriverCanUseCarsList(@RequestBody PageRequest pageRequest){
        PageResult<DriverCanUseCarsDTO>  driverCanUseCarsList = iDriverInfoService.getDriverCanCar(pageRequest.getPageNum(),
                pageRequest.getPageSize(),pageRequest.getDriverId(),pageRequest.getState(),pageRequest.getSearch());
        return ApiResponse.success(driverCanUseCarsList);
    }
    /**
     *驾驶员失效列表
     * @param pageRequest
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "查询驾驶员失效列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="getDriverLoseList" ,notes="查询驾驶员失效列表", httpMethod = "POST")
    @PostMapping("/getDriverLoseList")
    public ApiResponse<PageResult<DriverLoseDTO>> getDriverLoseList(@RequestBody PageRequest pageRequest){
        PageResult<DriverLoseDTO>  driverLoseList = iDriverInfoService.getDriverLoseList(pageRequest.getPageNum(),
                pageRequest.getPageSize(),pageRequest.getCarGroupId(),pageRequest.getSearch());
        return ApiResponse.success(driverLoseList);
    }
    /**
     *驾驶员失效数量
     * @param driverDTO
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "查询驾驶员失效数量", businessType = BusinessType.OTHER)
    @ApiOperation(value="getDriverLoseCount" ,notes="查询驾驶员失效数量", httpMethod = "POST")
    @PostMapping("/getDriverLoseCount")
    public ApiResponse  getDriverLoseCount(@RequestBody DriverNewDTO driverDTO){
        Long driverCount = iDriverInfoService.getDriverLoseCount(driverDTO.getDeptId());
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
    @Log(title = "驾驶员管理模块",content = "删除驾驶员", businessType = BusinessType.DELETE)
    @ApiOperation(value="getDriverDelete" ,notes="删除驾驶员", httpMethod = "POST")
    @PostMapping("/getDriverDelete")
    public ApiResponse  getDriverDelete(@RequestBody DriverNewDTO driverDTO) throws Exception {
        try {
            return iDriverInfoService.deleteDriver(driverDTO.getDriverId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     *修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    @Log(title = "驾驶员管理模块:修改驾驶员",content = "修改驾驶员", businessType = BusinessType.UPDATE)
    @ApiOperation(value="getDriverUpdate" ,notes="修改驾驶员", httpMethod = "POST")
    @PostMapping("/getDriverUpdate")
    public ApiResponse  getDriverUpdate(@RequestBody DriverCreateInfo driverCreateInfo){

            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            driverCreateInfo.setOptUserId(loginUser.getUser().getUserId());
      		boolean createDriver = driverInfoService.updateDriver(driverCreateInfo);
      		if(createDriver){
      			return ApiResponse.success();
             }else{
      			return ApiResponse.error();
             }
    }

    /**
     *修改驾驶员手机号
     * @param driverNewDTO
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "修改驾驶员手机号", businessType = BusinessType.UPDATE)
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
    @Log(title = "驾驶员管理模块", content = "设置驾驶员离职日期",businessType = BusinessType.UPDATE)
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
    @Log(title = "驾驶员管理模块:解绑驾驶员车辆", businessType = BusinessType.DELETE)
    @ApiOperation(value = "removeDriversCar",notes = "解绑驾驶员车辆",httpMethod ="POST")
    @PostMapping("/removeDriversCar")
    public ApiResponse removeDriversCar(@RequestBody CarDto carDto){
        try {
            driverCarRelationInfoService.removeCarDriver(carDto.getCarId(),carDto.getDriverId());
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
    @Log(title = "驾驶员管理模块:驾驶员绑定车辆", businessType = BusinessType.INSERT)
    @ApiOperation(value = "bindDriverCars",notes = "驾驶员绑定车辆",httpMethod ="POST")
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
     * 按月获取司机的排班详情
     * @return
     */
    @Log(title = "按月获取司机的排班详情",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
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
    @Log(title = "按月变更司机的排班",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
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
    /**
     * 按月获取排班详情_全部司机
     * @return
     */
    @Log(title = "驾驶员管理模块:按月获取全部司机排班详情", businessType = BusinessType.OTHER)
    @PostMapping("/getMonthWorkDetail")
    public ApiResponse<List<WorkInfoMonthVo>> getMonthWorkDetail(@RequestParam("month") String month){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        List<WorkInfoMonthVo> workInfoMonthList = null;

        workInfoMonthList = driverWorkInfoService.getWorkInfoMonthList(month,companyId);
        try {
            if( StringUtils.isBlank(month)){
                throw new Exception("参数异常");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success(workInfoMonthList);
    }

    /**
     * 按月变更排班_全部司机
     * @param workInfoDetailVo
     * @return
     */
    @Log(title = "驾驶员管理模块:按月变更全部司机排班", businessType = BusinessType.UPDATE)
    @PostMapping("/updateWorkDetailMonth")
    public ApiResponse updateWorkDetailMonth(@RequestBody WorkInfoDetailVo workInfoDetailVo){
        try {
            //获取登录用户
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            driverWorkInfoService.updateWorkDetailMonth(workInfoDetailVo,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("排班变更失败");
        }
        return ApiResponse.success("排班变更成功");
    }

    /**
     * 补单驾驶员列表
     * @param pageRequest
     * @return
     */
    @Log(title = "补单驾驶员列表",content = "查询补单驾驶员列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="supplementObtainDriver" ,notes="查询补单驾驶员列表", httpMethod = "POST")
    @PostMapping("/supplementObtainDriver")
    public ApiResponse<List<DriverInfo>> supplementObtainDriver(@RequestBody DriverInfo driverInfo){

        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        driverInfo.setCompanyId(Long.valueOf(loginUser.getUser().getDept().getCompanyId()));
        List<DriverInfo>  driverCanUseCarsList = iDriverInfoService.supplementObtainDriver(driverInfo);
        return ApiResponse.success(driverCanUseCarsList);
    }
}
