package com.hq.ecmp.ms.api.controller.apply;


import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApplyStateConstant;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.service.ChinaCityService;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 分页查询用车申请列表
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getUseApplySearchList",notes = "分页查询用车申请列表",httpMethod ="POST")
    @Log(title = "用车申请", content = "用车申请列表",businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplySearchList")
    public ApiResponse<PageResult<UserApplySingleVo>> getUseApplySearchList(@RequestBody UserApplySingleVo userApplySingleVo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<UserApplySingleVo> list = orderInfoTwoService.getUseApplySearchList(userApplySingleVo,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询公告列表失败");
        }
    }

    /**
     * 撤消申请单
     * @param  journeyApplyDto  撤消申请单
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "撤消申请单", businessType = BusinessType.OTHER)
    @ApiOperation(value = "revokeApplySingle",notes = "撤消申请单",httpMethod ="POST")
    @PostMapping("/revokeApplySingle")
    public ApiResponse revokeApplySingle(@RequestBody JourneyApplyDto journeyApplyDto){
        //撤销行程申请
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            int i = applyInfoService.updateApplyOrderState(journeyApplyDto.getApplyId(), ApplyStateConstant.CANCEL_APPLY, ApproveStateEnum.CANCEL_APPROVE_STATE.getKey(),userId);
            if(i == 1){
                return ApiResponse.success("撤销成功");
            }else {
                return ApiResponse.error("撤销申请失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("撤销申请失败，请重试");
        }
    }

    /**
     * 申请单提交根据名称搜索城市
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
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "提交申请单", businessType = BusinessType.OTHER)
    @ApiOperation(value = "submitApplySingle",notes = "提交申请单",httpMethod ="POST")
    @PostMapping("/submitApplySingle")
    public ApiResponse submitApplySingle(@RequestBody ApplySingleVO applySingleVO){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            int i = applyInfoService.submitApplySingle(loginUser,applySingleVO);
            if(i == 1){
                return ApiResponse.success("提交申请单成功");
            }else {
                return ApiResponse.error("提交申请单失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交申请单失败，请重试");
        }
    }

    /**
     * 查询制度中可用车型
     * @return
     */
    @ApiOperation(value = "queryCarTypeList",notes = "查询车型列表")
    @PostMapping("/queryCarTypeList")
    public ApiResponse<List<CarTypeVO>> queryCarTypeList(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            List<CarTypeVO> result = enterpriseCarTypeInfoService.queryCarTypeList(loginUser.getUser().getDept().getCompanyId());
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车型列表失败");
        }
    }

    /**
     * 分页查询待确认订单
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getToBeConfirmedOrderList",notes = "分页查询待确认订单",httpMethod ="POST")
    @Log(title = "用车申请", content = "分页查询待确认订单",businessType = BusinessType.OTHER)
    @PostMapping("/getToBeConfirmedOrderList")
    public ApiResponse<PageResult<UserApplySingleVo>> getToBeConfirmedOrderList(@RequestBody UserApplySingleVo userApplySingleVo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<UserApplySingleVo> list = orderInfoTwoService.getToBeConfirmedOrder(userApplySingleVo,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询公告列表失败");
        }
    }

    /**
     * 待确认订单----去确认
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "toSureToBeConfirmedOrder",notes = "待确认订单----去确认",httpMethod ="POST")
    @Log(title = "用车申请", content = "待确认订单----去确认",businessType = BusinessType.OTHER)
    @PostMapping("/toSureToBeConfirmedOrder")
    public ApiResponse toSureToBeConfirmedOrder(@RequestBody UserApplySingleVo userApplySingleVo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            int i = orderInfoTwoService.toSureToBeConfirmedOrder(userApplySingleVo,loginUser);
            if(i == 1){
                return ApiResponse.success("待确认订单----去确认成功");
            }else{
                return ApiResponse.error("待确认订单----去确认失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("待确认订单----去确认失败");
        }
    }

    /**
     * 获取当前业务员的待派车，已派车，已过期数量
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getUseApplyCounts",notes = "分页查询用车申请列表",httpMethod ="POST")
    @Log(title = "用车申请", content = "用车申请列表",businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplyCounts")
    public JSONObject getUseApplyCounts(@RequestBody UserApplySingleVo userApplySingleVo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        JSONObject jsonObject = new JSONObject();
        try {
            userApplySingleVo.setHomePageWaitingCarState("S100");
            List<UserApplySingleVo> waitingCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo,loginUser);
            jsonObject.put("waitingCarCount",waitingCarList.size());
            userApplySingleVo.setHomePageUsingCarState("S299");
            userApplySingleVo.setHomePageWaitingCarState("");
            List<UserApplySingleVo> usingCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo,loginUser);
            jsonObject.put("usingCarCount",usingCarList.size());
            userApplySingleVo.setHomePageExpireCarState("S005");
            userApplySingleVo.setHomePageUsingCarState("");
            userApplySingleVo.setHomePageWaitingCarState("");
            List<UserApplySingleVo> expireCarList = orderInfoTwoService.getUseApplyCounts(userApplySingleVo,loginUser);
            jsonObject.put("expireCarCount",expireCarList.size());
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
        }
        return jsonObject;
    }

}
