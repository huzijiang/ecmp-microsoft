package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private IEcmpOrgService ecmpOrgService;

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
    public ApiResponse<CarGroupDetailVO> getCarGroupDetail(@RequestBody CarGroupDTO carGroupDTO){
        try {
            CarGroupDetailVO carGroupDetailVO = carGroupInfoService.getCarGroupDetail(carGroupDTO.getCarGroupId());
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
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            String rtnMsg = carGroupInfoService.deleteCarGroup(carGroupDTO.getCarGroupId(),userId);
            return ApiResponse.success(rtnMsg);
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
    public ApiResponse disableCarGroup(@RequestBody Long carGroupId){
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
    public ApiResponse startUpCarGroup(@RequestBody Long carGroupId){
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
                    pageRequest.getPageSize(),pageRequest.getSearch(),
                    pageRequest.getState(),pageRequest.getDeptId(),pageRequest.getCarGroupId());
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

    /**
     * 查询指定城市所有车队调度员及车队座机/城市没有车队则查询公司所有车队调度员及车队座机
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupPhone",notes = "查询车队联系电话",httpMethod ="POST")
    @PostMapping("/getCarGroupPhone")
    public ApiResponse<List<CarGroupPhoneVO>> getCarGroupPhone(@RequestBody CarGroupPhoneDTO carGroupPhoneDTO){
        List<CarGroupPhoneVO>  list = null;
        try {
            list = carGroupInfoService.getCarGroupPhone(carGroupPhoneDTO.getCityCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }

    /**
     * 查询调度员电话机和调度员所在车队座机
     * @param
     * @return
     */
    @ApiOperation(value = "getDispatcherAndFixedLine",notes = "查询调度员电话机和调度员所在车队座机",httpMethod ="POST")
    @PostMapping("/getDispatcherAndFixedLine")
    public ApiResponse<DispatcherAndFixedLineVO> getDispatcherAndFixedLine(@RequestBody DispatcherAndFixedLineDTO
                                                                           dispatcherAndFixedLineDTO){
        DispatcherAndFixedLineVO  vo = null;
        try {
             vo = carGroupInfoService.getDispatcherAndFixedLine(dispatcherAndFixedLineDTO.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(vo);
    }

   /**
     * 查询公司车队树
     * @param
     * @return
     */
    @ApiOperation(value = "getCompanyCarGroupTree",notes = "公司车队树",httpMethod ="POST")
    @PostMapping("/getCompanyCarGroupTree")
    public ApiResponse<List<CompanyCarGroupTreeVO>> getCompanyCarGroupTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询车队列表
        List<CompanyCarGroupTreeVO>  list = null;
        try {
            list = ecmpOrgService.selectCompanyCarGroupTree(ecmpOrgDto.getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }

    /**
     * 查询  公司树
     * @param
     * @return
     */
    @ApiOperation(value = "getCompanyTree",notes = "公司树",httpMethod ="POST")
    @PostMapping("/getCompanyTree")
    public ApiResponse<List<CompanyTreeVO>> getCompanyTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询公司树
        List<CompanyTreeVO>  list = null;
        try {
            list = ecmpOrgService.getCompanyTree(ecmpOrgDto.getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }

    /**
     * 根据分/子公司id查询  车队树
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupTree",notes = "公司树",httpMethod ="POST")
    @PostMapping("/getCarGroupTree")
    public ApiResponse<List<CarGroupTreeVO>> getCarGroupTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询公司树
        List<CarGroupTreeVO>  list = null;
        try {
            list = carGroupInfoService.selectCarGroupTree(ecmpOrgDto.getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }


    /**
     * 车队人数统计
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupCount",notes = "车队人数统计",httpMethod ="POST")
    @PostMapping("/getCarGroupCount")
    public ApiResponse<CarGroupCountVO> getCarGroupCount(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询车队人数
        CarGroupCountVO carGroupCountVO = null;
        try {
            carGroupCountVO = ecmpOrgService.selectCarGroupCount(ecmpOrgDto.getDeptId());
            return ApiResponse.success(carGroupCountVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    /**
     * 判断车队编号是否存在
     * @param
     * @return
     */
    @ApiOperation(value = "judgeCarGroupCode",notes = "判断车队编号是否存在",httpMethod ="POST")
    @PostMapping("/judgeCarGroupCode")
    public ApiResponse<Boolean> judgeCarGroupCode(
            @RequestBody CarGroupDTO carGroupDTO){
        try {
           boolean exist = carGroupInfoService.judgeCarGroupCode(carGroupDTO.getCarGroupCode());
           return ApiResponse.success(exist);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("判断失败");
        }
    }

    /**
     * 车队信息回显
     * @param
     * @return
     */
    @ApiOperation(value = "getCarGroupInfoFeedBack",notes = "车队信息回显",httpMethod ="POST")
    @PostMapping("/getCarGroupInfoFeedBack")
    public ApiResponse<CarGroupDTO> getCarGroupInfoFeedBack(
            @RequestBody CarGroupDTO carGroupDTO){
        try {
            CarGroupDTO carGroupDTO1 = carGroupInfoService.getCarGroupInfoFeedBack(carGroupDTO.getCarGroupId());
            return ApiResponse.success(carGroupDTO1);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("回显失败");
        }
    }

}
