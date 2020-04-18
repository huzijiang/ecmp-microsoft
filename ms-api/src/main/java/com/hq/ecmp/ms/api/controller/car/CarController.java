package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarDriverDTO;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.dto.CarSaveDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.vo.CarDetailVO;
import com.hq.ecmp.mscore.vo.CarGroupCarInfo;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.DriverVO;
import com.hq.ecmp.mscore.vo.PageResult;
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
    @Autowired
    private ICarInfoService carInfoService;
    @Autowired
    private IDriverCarRelationInfoService driverCarRelationInfoService;

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
    @Log(title = "车型管理:企业车型列表", businessType = BusinessType.OTHER)
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

    /**
     * 新增车辆
     * @param
     * @return
     */
    @Log(title = "车辆管理:新增车辆", businessType = BusinessType.INSERT)
    @ApiOperation(value = "saveCar",notes = "新增车辆",httpMethod ="POST")
    @PostMapping("/saveCar")
    public ApiResponse<Long> saveCar(@RequestBody CarSaveDTO carSaveDTO){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        Long carId = null;
        try {
            carId = carInfoService.saveCar(carSaveDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
       return ApiResponse.success("新增车辆成功",carId);
    }

    /**
     * 修改车辆信息
     * @param
     * @return
     */
    @Log(title = "车辆管理:修改车辆", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "updateCar",notes = "修改车辆信息",httpMethod ="POST")
    @PostMapping("/updateCar")
    public ApiResponse updateCar(@RequestBody CarSaveDTO carSaveDTO){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
             carInfoService.updateCar(carSaveDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改信息失败");
        }
        return ApiResponse.success("修改车辆信息成功");
    }

    /**
     * 删除车辆
     * @param
     * @return
     */
    @Log(title = "车辆管理:删除车辆", businessType = BusinessType.DELETE)
    @ApiOperation(value = "deleteCar",notes = "删除车辆")
    @RequestMapping("/deleteCar")
    public ApiResponse deleteCar(@RequestBody CarDto carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carInfoService.deleteCarInfoById(carDto.getCarId(),userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("删除车辆成功");
    }

    /**
     * 车辆详情
     * @param
     * @return
     */
    @Log(title = "车辆管理:车辆详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "carDetail",notes = "车辆详情",httpMethod ="POST")
    @PostMapping("/carDetail")
    public ApiResponse<CarDetailVO> carDetail(@RequestBody CarDto carDto){

        try {
            CarDetailVO carDetailVO =  carInfoService.selectCarDetail(carDto.getCarId());
            return ApiResponse.success(carDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车辆详情失败");
        }

    }

    /**
     * 启用车辆  租赁/借调车辆到期无法启用，
     * @param  carDto  车辆信息
     * @return
     */
    @Log(title = "车辆管理:启用车辆", businessType = BusinessType.OTHER)
    @ApiOperation(value = "startCar",notes = "启用车辆",httpMethod ="POST")
    @PostMapping("/startCar")
    public ApiResponse startCar(@RequestBody CarDto carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carInfoService.startCar(carDto.getCarId(),userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("启用成功");
    }

    /**
     * 禁用车辆
     * @param  carDto  车辆信息
     * @return
     */
    @Log(title = "车辆管理:禁用车辆", businessType = BusinessType.OTHER)
    @ApiOperation(value = "disableCar",notes = "禁用车辆",httpMethod ="POST")
    @PostMapping("/disableCar")
    public ApiResponse disableCar(@RequestBody CarDto carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carInfoService.disableCar(carDto.getCarId(),userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("禁用成功");
    }

    /**
     * 维保车辆
     * @param  carDto  车辆信息
     * @return
     */
    @Log(title = "车辆管理:维保车辆", businessType = BusinessType.OTHER)
    @ApiOperation(value = "maintainCar",notes = "维保车辆",httpMethod ="POST")
    @PostMapping("/maintainCar")
    public ApiResponse maintainCar(@RequestBody CarDto carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carInfoService.maintainCar(carDto.getCarId(),userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改维保状态失败");
        }
        return ApiResponse.success("维保成功");
    }

    /**
     * 新增驾驶员
     * @param
     * @return
     */
    @Log(title = "车辆管理:绑定驾驶员", businessType = BusinessType.OTHER)
    @ApiOperation(value = "bindCarDrivers",notes = "车辆新增驾驶员",httpMethod ="POST")
    @PostMapping("/bindCarDriver")
    public ApiResponse bindCarDrivers(@RequestBody CarDriverDTO carDriverDTO){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            driverCarRelationInfoService.bindCarDrivers(carDriverDTO,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("新增驾驶员成功");
    }

    /**
     * 解绑车辆驾驶员
     * @param  carDto  车辆信息
     * @return
     */
    @Log(title = "车辆管理:解绑驾驶员", businessType = BusinessType.OTHER)
    @ApiOperation(value = "removeCarDriver",notes = "解绑车辆驾驶员",httpMethod ="POST")
    @PostMapping("/removeCarDriver")
    public ApiResponse removeCarDriver(@RequestBody CarDto carDto){
        try {
            driverCarRelationInfoService.removeCarDriver(carDto.getCarId(),carDto.getDriverId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("解绑成功");
    }

    /**
     * 获取车辆绑定驾驶员列表
     * @param
     * @return
     */
    @Log(title = "车辆管理:车辆驾驶员列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarDriverList",notes = "查看车辆已绑定驾驶员列表")
    @RequestMapping("/getCarDriverList")
    public ApiResponse<PageResult<DriverVO>> getCarDriverList(@RequestBody PageRequest pageRequest){
        PageResult<DriverVO> pageResult = null;
        try {
            pageResult = driverCarRelationInfoService.selectCarDriversByPage(pageRequest.getPageNum(),
                    pageRequest.getPageSize(), pageRequest.getCarId(),pageRequest.getWorkState(),pageRequest.getItIsFullTime(),pageRequest.getBusinessFlag());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询驾驶员列表信息失败");
        }
        return ApiResponse.success(pageResult);
    }

    /**
     * 按车队id查询车辆列表
     * @param
     * @return
     */
    @Log(title = "车辆管理:车队的车辆列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarListByGroup",notes = "按车队id查询车辆列表",httpMethod ="POST")
    @PostMapping("/getCarListByGroup")
    public ApiResponse<PageResult<CarListVO>> getCarListByGroup(@RequestBody PageRequest pageRequest){
        try {
           PageResult<CarListVO> list = carInfoService.selectCarListByGroup(pageRequest);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
    @Log(title = "车辆管理:车队下的可用车辆", businessType = BusinessType.OTHER)
    @ApiOperation(value = "carGroup", notes = "指定车队下的可用车辆", httpMethod = "POST")
	@PostMapping("/carGroup")
	public ApiResponse<CarGroupCarInfo> queryCarGroupCarList(@RequestBody Long carGroupId) {
		return ApiResponse.success(carInfoService.queryCarGroupCarList(carGroupId));
	}

    @Log(title = "车辆管理:车辆信息回显", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarInfoFeedBack", notes = "车辆信息回显", httpMethod = "POST")
    @PostMapping("/getCarInfoFeedBack")
    public ApiResponse<CarSaveDTO> getCarInfoFeedBack(@RequestBody CarDto carDto) {
       CarSaveDTO carSaveDTO = carInfoService.selectCarInfoFeedBack(carDto.getCarId());
       return ApiResponse.success(carSaveDTO);
    }

    @Log(title = "车辆管理:车辆所有品牌", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarTypeList", notes = "车辆品牌列表", httpMethod = "POST")
    @PostMapping("/getCarTypeList")
    public ApiResponse<List<String>> getCarTypeList() {
       List<String> carTypeList = carInfoService.selectCarTypeList();
        return ApiResponse.success(carTypeList);
    }
}
