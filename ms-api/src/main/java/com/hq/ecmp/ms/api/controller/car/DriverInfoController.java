package com.hq.ecmp.ms.api.controller.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.vo.PageResult;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/driverInfo")
public class DriverInfoController {
		@Autowired
		IDriverInfoService driverInfoService;
	
	
	@ApiOperation(value = "create", notes = "新增驾驶员", httpMethod = "POST")
	@PostMapping("/create")
	public ApiResponse create(@RequestBody DriverCreateInfo driverCreateInfo) {
		boolean createDriver = driverInfoService.createDriver(driverCreateInfo);
		if(createDriver){
			return ApiResponse.success();
		}else{
			return ApiResponse.error();
		}
	}
	
	@ApiOperation(value = "driverList", notes = "驾驶员列表", httpMethod = "POST")
	@PostMapping("/driverList")
	public ApiResponse<PageResult<DriverQueryResult>> driverList(@RequestBody DriverQuery driverQuery) {
		List<DriverQueryResult> list = driverInfoService.queryDriverList(driverQuery);
		Integer totalNum = driverInfoService.queryDriverListCount(driverQuery);
		PageResult<DriverQueryResult> pageResult = new PageResult<DriverQueryResult>(Long.valueOf(totalNum), list);
		return ApiResponse.success(pageResult);
	}
	
	@ApiOperation(value = "detail", notes = "驾驶员详情", httpMethod = "POST")
	@PostMapping("/detail")
	public ApiResponse<DriverQueryResult> detail(@RequestBody Long driverId) {
		return ApiResponse.success(driverInfoService.queryDriverDetail(driverId));
	}
}
