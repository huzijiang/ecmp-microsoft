package com.hq.ecmp.ms.api.controller.apply;


import com.alibaba.fastjson.JSONObject;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApplyStateConstant;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.dto.cost.ApplyPriceDetails;
import com.hq.ecmp.mscore.dto.cost.CarGroupInfoVo;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用车申请
 */
@RestController
@RequestMapping("/useApply")
public class UserApplySingleController {

    @Autowired
    TokenService tokenService;

    @Autowired
    private OrderInfoTwoService orderInfoTwoService;

    @Autowired
    private IApplyInfoService applyInfoService;

    @Autowired
    private ChinaCityService cityService;

    @Autowired
    private IEnterpriseCarTypeInfoService enterpriseCarTypeInfoService;

    @Autowired
    private ICostConfigInfoService costConfigInfoService;

    /**
     * 分页查询用车申请列表
     *
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getUseApplySearchList", notes = "分页查询用车申请列表", httpMethod = "POST")
    @Log(title = "用车申请", content = "用车申请列表", businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplySearchList")
    public ApiResponse<PageResult<UserApplySingleVo>> getUseApplySearchList(@RequestBody UserApplySingleVo userApplySingleVo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<UserApplySingleVo> list = orderInfoTwoService.getUseApplySearchList(userApplySingleVo, loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询用车申请列表失败");
        }
    }

    /**
     * 获取各种状态的申请单数量
     *
     * @return
     */
    @ApiOperation(value = "getApplyStateCount", notes = "分页查询用车申请列表", httpMethod = "POST")
    @Log(title = "用车申请", content = "用车申请列表", businessType = BusinessType.OTHER)
    @PostMapping("/getApplyStateCount")
    public ApiResponse<List<Map<String, String>>> getApplyStateCount() {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            List<Map<String, String>> vo = applyInfoService.getApplyStateCount(loginUser);
            return ApiResponse.success(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询用车申请列表失败");
        }
    }

    /**
     * 首页查询用车申请列表
     *
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getUseApplyList", notes = "首页查询用车申请列表", httpMethod = "POST")
    @Log(title = "用车申请", content = "用车申请列表", businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplyList")
    public ApiResponse<PageResult<UserApplySingleVo>> getUseApplyList(@RequestBody UserApplySingleVo userApplySingleVo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<UserApplySingleVo> list = orderInfoTwoService.getUseApplyList(userApplySingleVo, loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("首页查询用车申请列表失败");
        }
    }

    /**
     * 撤消申请单
     *
     * @param journeyApplyDto 撤消申请单
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "撤消申请单", businessType = BusinessType.OTHER)
    @ApiOperation(value = "revokeApplySingle", notes = "撤消申请单", httpMethod = "POST")
    @PostMapping("/revokeApplySingle")
    public ApiResponse revokeApplySingle(@RequestBody JourneyApplyDto journeyApplyDto) {
        //撤销行程申请
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            ApiResponse apiResponse = applyInfoService.updateApplyOrderState(journeyApplyDto.getApplyId(), ApplyStateConstant.CANCEL_APPLY, ApproveStateEnum.CANCEL_APPROVE_STATE.getKey(), userId);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("撤销申请失败，请重试");
        }
    }

    /**
     * 申请单提交根据名称搜索城市
     *
     * @return
     */
    @ApiOperation(value = "queryCityByName", notes = "名称模糊搜索城市列表 ", httpMethod = "POST")
    @PostMapping("/queryCityByName")
    public ApiResponse<List<CityInfo>> queryCityByName() {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            List<CityInfo> result = cityService.queryCityByName(loginUser.getUser().getDept().getCompanyId());
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("申请单提交根据名称搜索城市失败");
        }
    }

    /**
     * 提交申请单
     *
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "提交申请单", businessType = BusinessType.OTHER)
    @ApiOperation(value = "submitApplySingle", notes = "提交申请单", httpMethod = "POST")
    @PostMapping("/submitApplySingle")
    public ApiResponse submitApplySingle(@RequestBody ApplySingleVO applySingleVO) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            SysUser user = loginUser.getUser();
            UserVO applyUser = new UserVO(user.getUserId(), user.getNickName(), user.getPhonenumber());
            applySingleVO.setApplyUser(applyUser);
            ApiResponse apiResponse = applyInfoService.submitApplySingle(loginUser, applySingleVO);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交申请单失败，请重试");
        }
    }

    /**
     * 查询制度中可用车型
     *
     * @return
     */
    @ApiOperation(value = "queryCarTypeList", notes = "查询车型列表")
    @PostMapping("/queryCarTypeList")
    public ApiResponse<List<CarTypeVO>> queryCarTypeList() {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            List<CarTypeVO> result = enterpriseCarTypeInfoService.queryCarTypeList(loginUser.getUser().getDept().getCompanyId());
            return ApiResponse.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车型列表失败");
        }
    }

    /**
     * 分页查询待确认订单
     *
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getToBeConfirmedOrderList", notes = "分页查询待确认订单", httpMethod = "POST")
    @Log(title = "用车申请", content = "分页查询待确认订单", businessType = BusinessType.OTHER)
    @PostMapping("/getToBeConfirmedOrderList")
    public ApiResponse<PageResult<UserApplySingleVo>> getToBeConfirmedOrderList(@RequestBody UserApplySingleVo userApplySingleVo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<UserApplySingleVo> list = orderInfoTwoService.getToBeConfirmedOrder(userApplySingleVo, loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询公告列表失败");
        }
    }

    /**
     * 待确认订单----去确认
     *
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "toSureToBeConfirmedOrder", notes = "待确认订单----去确认", httpMethod = "POST")
    @Log(title = "用车申请", content = "待确认订单----去确认", businessType = BusinessType.OTHER)
    @PostMapping("/toSureToBeConfirmedOrder")
    public ApiResponse toSureToBeConfirmedOrder(@RequestBody UserApplySingleVo userApplySingleVo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            int i = orderInfoTwoService.toSureToBeConfirmedOrder(userApplySingleVo, loginUser);
            if (i == 1) {
                return ApiResponse.success("待确认订单----去确认成功");
            } else {
                return ApiResponse.error("待确认订单----去确认失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("待确认订单----去确认失败");
        }
    }

    /**
     * 获取当前业务员的待派车，已派车，已过期数量
     *
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getUseApplyCounts", notes = "分页查询用车申请列表", httpMethod = "POST")
    @Log(title = "用车申请", content = "用车申请列表", businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplyCounts")
    public JSONObject getUseApplyCounts(@RequestBody UserApplySingleVo userApplySingleVo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        JSONObject jsonObject = new JSONObject();
        try {
            userApplySingleVo.setHomePageWaitingCarState("S100");
            List<UserApplySingleVo> waitingCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo, loginUser);
            jsonObject.put("waitingCarCount", waitingCarList.size());
            userApplySingleVo.setHomePageUsingCarState("S299");
            userApplySingleVo.setHomePageWaitingCarState("");
            List<UserApplySingleVo> usingCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo, loginUser);
            jsonObject.put("usingCarCount", usingCarList.size());
            userApplySingleVo.setHomePageExpireCarState("S005");
            userApplySingleVo.setHomePageUsingCarState("");
            userApplySingleVo.setHomePageWaitingCarState("");
            List<UserApplySingleVo> expireCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo, loginUser);
            jsonObject.put("expireCarCount", expireCarList.size());
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询员工订单动态失败");
        }
        return jsonObject;
    }

    /**
     * 外部车队列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "applySingleCarGroupList", notes = "外部车队列表", httpMethod = "POST")
    @Log(title = "外部车队列表", content = "外部车队列表", businessType = BusinessType.OTHER)
    @PostMapping("/applySingleCarGroupList")
    public ApiResponse<List<CarGroupInfo>> applySingleCarGroupList() {
        List<CarGroupInfo> carGroupInfos = new ArrayList<>();
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            carGroupInfos = orderInfoTwoService.applySingleCarGroupList(loginUser.getUser().getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取车队列表失败");
        }
        return ApiResponse.success(carGroupInfos);
    }

    /**
     * 获取价格计划详情
     *
     * @return
     */
    @ApiOperation(value = "applySinglePriceDetails", notes = "获取价格计划详情", httpMethod = "POST")
    @Log(title = "获取价格计划详情", content = "获取价格计划详情", businessType = BusinessType.OTHER)
    @PostMapping("/applySinglePriceDetails")
    public ApiResponse<List<CarGroupInfoVo>> applySinglePriceDetails(@RequestBody ApplyPriceDetails applyPriceDetail) {
        List<CarGroupInfoVo> applyPriceDetails = new ArrayList<>();
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            applyPriceDetail.setCompanyId(loginUser.getUser().getDept().getCompanyId());
            applyPriceDetails = costConfigInfoService.applySinglePriceDetails(applyPriceDetail, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取价格计划详情失败");
        }
        return ApiResponse.success(applyPriceDetails);
    }

    /**
     * 修改申请单
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "修改申请单", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateApplySingle", notes = "修改申请单", httpMethod = "POST")
    @PostMapping("/updateApplySingle")
    public ApiResponse updateApplySingle(@RequestBody ApplySingleVO applySingleVO) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            ApiResponse apiResponse = applyInfoService.updateApplySingle(loginUser, applySingleVO);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交申请单失败，请重试");
        }
    }

    /**
     * 获取申请单详情
     */
    @ApiOperation(value = "获取申请单详情", httpMethod = "POST")
    @RequestMapping("/getApplyInfoDetail")
    public ApiResponse<ApplySingleVO> getApplyInfoDetail(@RequestParam(value = "applyId") Long applyId) {
        try {
            ApplySingleVO applySingleVO = applyInfoService.getApplyInfoDetail(applyId);
            return ApiResponse.success(applySingleVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }
}
