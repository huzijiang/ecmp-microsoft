package com.hq.ecmp.ms.api.controller.car;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.ecmp.constant.JourneyUserCarPowerConstant;
import com.hq.ecmp.mscore.domain.JourneyUserCarPower;
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
	
	
	@ApiOperation(value = "createUseCarAuthority", notes = "生成用车权限", httpMethod = "POST")
	@PostMapping("/createUseCarAuthority")
	public ApiResponse createUseCarAuthority(Long applyId,Long userId) {
		boolean optFlag = journeyUserCarPowerService.createUseCarAuthority(applyId,userId);
		 if(!optFlag){
			 return ApiResponse.error("生成用车权限失败");
		 }
		return ApiResponse.success();
	}

	@ApiOperation(value = "公务用车过期权限和驳回权限已读",tags = "公务用车过期权限和驳回权限已读",httpMethod = "POST")
	@PostMapping("/officialOverTimePowerIsRead")
	public  ApiResponse officialOverTimePowerIsRead(Long ticketId){
		try {
			JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
			journeyUserCarPower.setPowerId(ticketId);
			journeyUserCarPower.setIsRead(JourneyUserCarPowerConstant.IS_READ_YES);
			int i = journeyUserCarPowerService.updateJourneyUserCarPower(journeyUserCarPower);
			if(i!=1){
				return  ApiResponse.error("用车权限读取状态变更失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  ApiResponse.error("用车权限读取状态变更失败");
		}
		return ApiResponse.success("用车权限读取状态变更成功");
	}
	
}
