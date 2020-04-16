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
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.dto.CarTypeSortDTO;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.vo.CarTypeVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param
     * @return
     */
    @Log(title = "车型管理:新增车型", businessType = BusinessType.INSERT)
    @ApiOperation(value = "saveCarType",notes = "新增车型",httpMethod ="POST")
    @PostMapping("/saveCarType")
    public ApiResponse saveCarType(@RequestBody CarTypeDTO carTypeDto){
        Long userId = getLoginUserId();
        try {
            enterpriseCarTypeInfoService.saveCarType(carTypeDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("新增成功");
    }

    /**
     * 获取登录用户
     * @return
     */
    private Long getLoginUserId() {
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        return loginUser.getUser().getUserId();
    }

    /**
     * 修改车型
     * @param
     * @return
     */
    @Log(title = "车型管理:修改车型", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "updateCarType",notes = "修改车型",httpMethod ="POST")
    @PostMapping("/updateCarType")
    public ApiResponse updateCarType(@RequestBody CarTypeDTO carTypeDto){
        //获取登录用户
        Long userId = getLoginUserId();
        try {
            enterpriseCarTypeInfoService.updateCarType(carTypeDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改失败");
        }
        return ApiResponse.success("修改成功");
    }

    /**
     * 删除车型
     * @param
     * @return
     */
    @Log(title = "车型管理:删除车型", businessType = BusinessType.DELETE)
    @ApiOperation(value = "deleteCarType",notes = "删除车型")
    @PostMapping("/deleteCarType")
    public ApiResponse deleteCarType(@RequestBody Long carTypeId){
        try {
            enterpriseCarTypeInfoService.deleteEnterpriseCarTypeInfoById(carTypeId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("删除车型成功");
    }

    /**
     * 查询车型列表
     * @param
     * @return
     */
    @Log(title = "车型管理:车型列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarTypeList",notes = "查询车型列表")
    @PostMapping("/getCarTypeList")
    public ApiResponse<List<CarTypeVO>> getCarTypeList(@RequestBody Long enterpriseId){
        try {
            List<CarTypeVO> result = enterpriseCarTypeInfoService.getCarTypeList(enterpriseId);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车型列表失败");
        }
    }

    /**
     * 车型排序修改
     * @param
     * @return
     */
    @Log(title = "车型管理:车型排序", businessType = BusinessType.OTHER)
    @ApiOperation(value = "sortCarType",notes = "车型排序")
    @PostMapping("/sortCarType")
    public ApiResponse sortCarType(@RequestBody CarTypeSortDTO carTypeSortDTO){
        try {
            enterpriseCarTypeInfoService.sortCarType(
                    carTypeSortDTO.getMainCarTypeId(),carTypeSortDTO.getTargetCarTypeId(),getLoginUserId()
            );
            return ApiResponse.success("车型排序成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("车型排序失败");
        }
    }
}
