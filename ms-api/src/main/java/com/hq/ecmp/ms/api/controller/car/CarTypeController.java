package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.ms.api.dto.car.DriverDto;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 12:11
 */
@RestController
@RequestMapping("/carType")
public class CarTypeController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEnterpriseCarTypeInfoService enterpriseCarTypeInfoService;

    /**
     * 新增车型
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "saveCarType",notes = "新增车型",httpMethod ="POST")
    @PostMapping("/saveCarType")
    public ApiResponse saveCarType(@RequestBody CarTypeDTO carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            enterpriseCarTypeInfoService.saveCarType(carDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("新增成功");
    }

    /**
     * 修改车型
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "updateCarType",notes = "新增车型",httpMethod ="POST")
    @PostMapping("/updateCarType")
    public ApiResponse updateCarType(@RequestBody CarTypeDTO carDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            enterpriseCarTypeInfoService.updateCarType(carDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改失败");
        }
        return ApiResponse.success("修改成功");
    }

    /**
     * 删除车型
     * @param  carDto  车辆信息
     * @return
     */
    @ApiOperation(value = "deleteCarType",notes = "删除车型")
    @RequestMapping("/deleteCarType")
    public ApiResponse deleteCarType(@RequestBody CarTypeDTO carDto){
        try {
            enterpriseCarTypeInfoService.deleteEnterpriseCarTypeInfoById(carDto.getCarTypeId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("删除车型成功");
    }
}
