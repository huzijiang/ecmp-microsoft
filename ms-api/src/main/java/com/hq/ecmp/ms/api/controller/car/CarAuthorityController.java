package com.hq.ecmp.ms.api.controller.car;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.CarAuthorityInfo;
import com.hq.ecmp.mscore.domain.ServiceTypeCarAuthority;
import com.hq.ecmp.mscore.domain.UserAuthorityGroupCity;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;

import io.swagger.annotations.ApiOperation;

/**
 * 用车权限
 * @author cm
 *
 */
@RestController
@RequestMapping("/car/authority")
public class CarAuthorityController {
	@Autowired
	private IJourneyInfoService journeyInfoService;
	@Autowired
	private IJourneyUserCarPowerService journeyUserCarPowerService;
	@Autowired
    private TokenService tokenService;
	
	@ApiOperation(value = "list", notes = "获取用户所有的用车权限", httpMethod = "POST")
	@PostMapping("/list")
	public ApiResponse<List<CarAuthorityInfo>> getUserCarAuthorityList() {
		 HttpServletRequest request = ServletUtils.getRequest();
	        LoginUser loginUser = tokenService.getLoginUser(request);
		return ApiResponse.success(journeyInfoService.getUserCarAuthorityList(loginUser.getUser().getUserId()));
	}
	
	
	@ApiOperation(value = "user", notes = "获取指定行程下的的所有行程节点生成的用车权限", httpMethod = "POST")
	@PostMapping("/user")
	public ApiResponse<List<UserAuthorityGroupCity>> getUserCarAuthority(Long journeyId) {
		 return ApiResponse.success(journeyInfoService.getUserCarAuthority(journeyId));
	}
	
	@ApiOperation(value = "getAuthorityFromService", notes = "获取指定服务类型下的用户用车权限", httpMethod = "POST")
	@PostMapping("/getAuthorityFromService")
	public ApiResponse<List<ServiceTypeCarAuthority>> getAuthorityFromService(String type,Long journeyId) {
		 return ApiResponse.success(journeyUserCarPowerService.queryUserAuthorityFromService(type,journeyId));
	}
	
}
