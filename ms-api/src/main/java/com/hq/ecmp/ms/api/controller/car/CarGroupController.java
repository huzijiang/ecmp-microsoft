package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.SubGroupListDTO;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.vo.CarGroupDetailVO;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.vo.PageResult;
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
     * 新增车队（车队作为部门，同时也要新增部门 ）
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
    public ApiResponse updateCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.updateCarGroup(carGroupDTO,userId);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改车队失败");
        }
    }

    /**
     * 删除车队
     * @param
     * @return
     */
    @ApiOperation(value = "deleteCarGroup",notes = "删除车队",httpMethod ="POST")
    @PostMapping("/deleteCarGroup")
    public ApiResponse deleteCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        try {
            carGroupInfoService.deleteCarGroup(carGroupDTO.getCarGroupId());
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 禁用车队
     * @param
     * @return
     */
    @ApiOperation(value = "disableCarGroup",notes = "禁用车队",httpMethod ="POST")
    @PostMapping("/disableCarGroup")
    public ApiResponse disableCarGroup(Long carGroupId){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.disableCarGroup(carGroupId,userId);
            return ApiResponse.success("禁用车队成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 启用车队
     * @param
     * @return
     */
    @ApiOperation(value = "startUpCarGroup",notes = "启用车队",httpMethod ="POST")
    @PostMapping("/startUpCarGroup")
    public ApiResponse startUpCarGroup(Long carGroupId){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.startUpCarGroup(carGroupId,userId);
            return ApiResponse.success("启用车队成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("启用车队失败");
        }
    }

    /**
     * 分页全部查询车队列表（带搜索功能）
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupList",notes = "分页全部查询车队列表",httpMethod ="POST")
    @PostMapping("/getCarGroupList")
    public ApiResponse<PageResult<CarGroupListVO>> getCarGroupList(@RequestBody PageRequest pageRequest){
        try {
            PageResult<CarGroupListVO> list = carGroupInfoService.selectCarGroupInfoByPage(pageRequest.getPageNum(),
                    pageRequest.getPageSize(),pageRequest.getSearch());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车队列表失败");
        }
    }

    /**
     * 查询下级车队列表
     * @param
     * @return
     */
    @ApiOperation(value = "getSubCarGroupList",notes = "启用车队",httpMethod ="POST")
    @PostMapping("/getSubCarGroupList")
    public ApiResponse<List<CarGroupListVO>> getSubCarGroupList(@RequestBody SubGroupListDTO subGroupListDTO){
        try {
            List<CarGroupListVO> list = carGroupInfoService.selectSubCarGroupInfoList(subGroupListDTO.getDeptId());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询下级车队列表失败");
        }
    }
}
