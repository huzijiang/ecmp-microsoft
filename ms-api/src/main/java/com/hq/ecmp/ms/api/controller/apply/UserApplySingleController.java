package com.hq.ecmp.ms.api.controller.apply;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.UserApplySingleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 分页查询待确认订单
     * @param userApplySingleVo
     * @return
     */
    @ApiOperation(value = "getToBeConfirmedOrderList",notes = "分页查询用车申请列表",httpMethod ="POST")
    @Log(title = "用车申请", content = "用车申请列表",businessType = BusinessType.OTHER)
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
