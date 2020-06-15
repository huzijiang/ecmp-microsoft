package com.hq.ecmp.ms.api.controller.driver;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.car.CarDto;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 驾驶员信息
 * @author :shixin
 * @Date： 2020-3-20
 */
@RestController
@RequestMapping("/driverBack")
public class DriverBackController {
    @Autowired
    IDriverInfoService driverInfoService;
    @Autowired
    TokenService tokenService;
    @Autowired
    ICarGroupInfoService carGroupInfoService;
    @Autowired
    ICarInfoService carInfoService;
    @Autowired
    IEcmpOrgService ecmpOrgService;
    @Autowired
    IDispatchService dispatchService;


    /**
     *驾驶员调度看板(分页列表)
     * @param pageRequest
     * @return
     */
    @Log(title = "驾驶员调度看板",content = "查询驾驶员可用车辆列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="driverWorkOrderList" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/driverWorkOrderList")
    public ApiResponse<PageResult<DriverOrderVo>> driverWorkOrderList(@RequestBody PageRequest pageRequest){
        PageResult pageResult=driverInfoService.driverWorkOrderList(pageRequest);
        return ApiResponse.success(pageResult);
    }

    /**
     *获取当前人权限下的所有车队
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "获取当前人权限下的所有车队", businessType = BusinessType.OTHER)
    @ApiOperation(value="getCarGroupList" ,notes="获取当前人权限下的所有车队", httpMethod = "POST")
    @PostMapping("/getCarGroupList")
    public ApiResponse<List<CarGroupListVO>> getCarGroupList(@RequestParam(value = "cityCode" ,required = false) String cityCode){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<CarGroupListVO> list=carGroupInfoService.getCarGroupList(loginUser.getUser(),cityCode);
        return ApiResponse.success(list);
    }

    /**
     *车辆调度看板(分页列表)
     * @param pageRequest
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "查询驾驶员可用车辆列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="carWorkOrderList" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/carWorkOrderList")
    public ApiResponse<PageResult<DriverOrderVo>> carWorkOrderList(@RequestBody PageRequest pageRequest){
        PageResult pageResult=carInfoService.carWorkOrderList(pageRequest);
        return ApiResponse.success(pageResult);
    }

    /**
     *驾驶员调度看板(分页列表)
     * @return
     */
    @Log(title = "驾驶员管理模块",content = "查询驾驶员可用车辆列表", businessType = BusinessType.OTHER)
    @ApiOperation(value="getOrderStateCount" ,notes="查询驾驶员可用车辆列表", httpMethod = "POST")
    @PostMapping("/getOrderStateCount")
    public ApiResponse<OrderStateCountVO> getOrderStateCount(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
//        Long userId = loginUser.getUser().getUserId();
//        Long orgComcany=null;
//        EcmpOrg ecmpOrg = ecmpOrgService.getOrgByDeptId(loginUser.getUser().getDeptId());
//        if (ecmpOrg!=null){
//            orgComcany=ecmpOrg.getDeptId();
//        }
        OrderStateCountVO pageResult=dispatchService.getOrderStateCount(loginUser.getUser());
        return ApiResponse.success(pageResult);
    }
}
