package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.vo.CarGroupDetailVO;
import com.hq.ecmp.mscore.vo.UserVO;
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
 * @Date: 2020/3/12 10:24
 */
@RestController
@RequestMapping("/cargroup")
public class CarGroupController {

    @Autowired
    private ICarGroupInfoService carGroupInfoService;
    @Autowired
    private TokenService tokenService;

    /**
     * 新增车队
     * @param  carGroupDTO 车队信息
     * @return
     */
    @ApiOperation(value = "saveCarGroup",notes = "新增车队",httpMethod ="POST")
    @PostMapping("/saveCarGroup")
    public ApiResponse saveCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
          carGroupInfoService.saveCarGroupAndDispatcher(carGroupDTO,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("保存车队信息失败");
        }
        return ApiResponse.success("保存成功");
    }

    /**
     * 车队详情
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupDetail",notes = "车队详情",httpMethod ="POST")
    @PostMapping("/getCarGroupDetail")
    public ApiResponse<CarGroupDetailVO> getCarGroupDetail(Long carGroupId){
        try {
            CarGroupDetailVO carGroupDetailVO = carGroupInfoService.getCarGroupDetail(carGroupId);
            return ApiResponse.success(carGroupDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车队详情失败");
        }
    }

    /**
     * 修改车队
     * @param
     * @return
     */
    @ApiOperation(value = "updateCarGroup",notes = "修改车队",httpMethod ="POST")
    @PostMapping("/updateCarGroup")
    public ApiResponse<CarGroupDetailVO> updateCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.updateCarGroup(carGroupDTO,userId);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除车队失败");
        }
    }

    /**
     * 禁用车队
     * @param
     * @return
     */
    @ApiOperation(value = "updateCarGroup",notes = "修改车队",httpMethod ="POST")
    @PostMapping("/updateCarGroup")
    public ApiResponse<CarGroupDetailVO> disableCarGroup(Long carGroupId){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.disableCarGroup(carGroupId,userId);
            return ApiResponse.success("禁用车队成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("禁用车队失败");
        }
    }
}
