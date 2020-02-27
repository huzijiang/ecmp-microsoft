package com.hq.ecmp.ms.api.controller.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarAuthorityInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.impl.JourneyInfoServiceImpl;

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
	
	@ApiOperation(value = "list", notes = "获取用户所有的用车权限", httpMethod = "POST")
	@PostMapping("/list")
	public ApiResponse<List<CarAuthorityInfo>> getUserCarAuthorityList(Integer userId) {
		return ApiResponse.success(journeyInfoService.getUserCarAuthorityList(userId));
	}
	
	

}
