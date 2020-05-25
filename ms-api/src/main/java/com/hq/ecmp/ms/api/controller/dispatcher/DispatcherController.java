package com.hq.ecmp.ms.api.controller.dispatcher;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.DispatchVo;
import com.hq.ecmp.mscore.vo.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName
 * @Description TODO 佛山包车业务调度模块
 * @Author yj
 * @Date 2020/5/24 10:40
 * @Version 1.0
 */
@RestController("/dispatch/charterCar")
public class DispatcherController {

    @Resource
    private OrderInfoTwoService orderInfoTwoService;
    @Resource
    private TokenService tokenService;

    /**
     * 获取调度列表数据
     */
    @PostMapping("/getDispatcherList")
    public ApiResponse<PageResult<DispatchVo>>  getDispatcherList(@RequestBody ApplyDispatchQuery query){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            PageResult<DispatchVo> list = orderInfoTwoService.queryDispatchListCharterCar(query,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取申请调度列表失败");
        }
    }

}
