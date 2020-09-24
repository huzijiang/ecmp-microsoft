package com.hq.ecmp.ms.api.controller.statistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.api.system.domain.SysDept;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.dto.UseCarDataByGroupDto;
import com.hq.ecmp.mscore.dto.UseCarDataDto;
import com.hq.ecmp.mscore.dto.UserDeptUseCarDetailDto;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.StatisticsForAdminDetailVo;
import com.hq.ecmp.mscore.vo.UseCarDataVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/9 14:38
 */
@RestController
@RequestMapping("/useCar")
@Slf4j
public class UseCarStatisticsController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private OrderInfoTwoService orderInfoTwoService;
    @Autowired
    private IOrderStateTraceInfoService orderStateTraceInfoService;

    /**
     *服务用户部门的车队列表（包含内外部车队）
     * @param
     * @return
     */
    @ApiOperation(value = "userDeptCarGroupList",notes = "服务用户部门的车队列表",httpMethod ="POST")
    @Log(title = "服务用户部门的车队列表", content = "用户部门车队列表",businessType = BusinessType.OTHER)
    @PostMapping("/userDeptCarGroupList")
    public ApiResponse<List<CarGroupInfo>> userDeptCarGroupList(){
        List<CarGroupInfo> carGroupInfos = new ArrayList<>();
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            carGroupInfos = orderInfoTwoService.userDeptCarGroupList(loginUser.getUser().getDeptId());
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("获取用户车队列表失败");
        }
        return  ApiResponse.success(carGroupInfos);
    }

    @ApiOperation(value = "userDeptUseCarData",notes = "用户部门用车统计数据",httpMethod ="POST")
    @Log(title = "查询用户部门用车统计数据", content = "查询用户部门用车统计数据",businessType = BusinessType.OTHER)
    @PostMapping("/deptUseCarData")
    public ApiResponse<UseCarDataVo> deptUseCarData(@RequestBody UseCarDataDto useCarDataDto){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
          //  log.info("请求参数：{}，请求用户：{}", JSONArray.toJSON(useCarDataDto).toString(),loginUser.getUser().getPhonenumber());
            UseCarDataVo useCarDataVo =  orderStateTraceInfoService.selectDeptUseCarData(useCarDataDto,loginUser);
            return ApiResponse.success("查询成功",useCarDataVo);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("微服务查询用户部门用车统计数据失败");
        }
    }


    @ApiOperation(value = "userDeptUseCarDataByCarGroup",notes = "按车队分页查询用户部门用车统计数据",httpMethod ="POST")
    @Log(title = "按车队分页查询用户部门用车统计数据", content = "按车队分页查询用户部门用车统计数据",businessType = BusinessType.OTHER)
    @PostMapping("/userDeptUseCarDataByCarGroup")
    public ApiResponse<PageResult<UseCarDataVo>> userDeptUseCarDataByCarGroup(@RequestBody UseCarDataByGroupDto useCarDataByGroupDto){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            //log.info("请求参数：{}，请求用户：{}", JSONArray.toJSON(useCarDataByGroupDto).toString(),loginUser.getUser().getPhonenumber());
            PageResult<UseCarDataVo> result =  orderStateTraceInfoService.userDeptUseCarDataByCarGroup(useCarDataByGroupDto,loginUser);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("微服务查询用户部门用车统计数据失败");
        }
    }


    @ApiOperation(value = "userDeptUseCarDetail",notes = "按车队分页查询用户部门用车统计订单列表",httpMethod ="POST")
    @Log(title = "按车队分页查询用户部门用车统计订单列表", content = "按车队分页查询用户部门用车统计订单列表",businessType = BusinessType.OTHER)
    @PostMapping("/userDeptUseCarDetail")
    public ApiResponse<PageResult<StatisticsForAdminDetailVo>> userDeptUseCarDetail(@RequestBody UserDeptUseCarDetailDto userDeptUseCarDetailDto){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            //log.info("请求参数：{}，请求用户：{}", JSONArray.toJSON(useCarDataByGroupDto).toString(),loginUser.getUser().getPhonenumber());
            PageResult<StatisticsForAdminDetailVo> result =  orderStateTraceInfoService.userDeptUseCarDetail(userDeptUseCarDetailDto,loginUser);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("微服务查询用户部门用车详情数据失败");
        }
    }

    @ApiOperation(value = "userDeptUseCarDetailExport",notes = "按车队分页查询用户部门用车统计订单列表导出",httpMethod ="POST")
    @Log(title = "按车队查询用户部门用车统计订单列表导出", content = "按车队分页查询用户部门用车统计订单列表导出",businessType = BusinessType.OTHER)
    @PostMapping("/userDeptUseCarDetailExport")
    public ApiResponse<List<StatisticsForAdminDetailVo>> userDeptUseCarDetailExport(@RequestBody UserDeptUseCarDetailDto userDeptUseCarDetailDto){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            log.info("userDeptUseCarDetailExport请求参数={},用户={}", JSONObject.toJSONString(userDeptUseCarDetailDto),JSONObject.toJSONString(loginUser));
            List<StatisticsForAdminDetailVo> result =  orderStateTraceInfoService.userDeptUseCarDetailExport(userDeptUseCarDetailDto,loginUser);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("微服务查询用户部门用车详情数据导出失败");
        }
    }

}
