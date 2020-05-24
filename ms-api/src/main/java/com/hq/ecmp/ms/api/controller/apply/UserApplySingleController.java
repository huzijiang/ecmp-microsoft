package com.hq.ecmp.ms.api.controller.apply;


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


    @ApiOperation(value = "getUseApplySearchList",notes = "分页查询用车申请列表",httpMethod ="POST")
    @Log(title = "用车申请", content = "用车申请列表",businessType = BusinessType.OTHER)
    @PostMapping("/getUseApplySearchList")
    public ApiResponse<PageResult<UserApplySingleVo>> getUseApplySearchList(@RequestBody UserApplySingleVo userApplySingleVo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            //PageResult<UserApplySingleVo> list = orderInfoTwoService.queryDispatchList(userApplySingleVo,loginUser);
            //return ApiResponse.success(list);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询公告列表失败");
        }
    }
}
