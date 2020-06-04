package com.hq.ecmp.ms.api.controller.car;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.DriverUserJobNumber;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.vo.PageResult;

import io.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@RequestMapping("/driverInfo")
public class DriverInfoController {
		@Autowired
		IDriverInfoService driverInfoService;
		@Autowired
		TokenService tokenService;

	@Log(title = "驾驶员管理:新增驾驶员", businessType = BusinessType.INSERT)
	@ApiOperation(value = "create", notes = "新增驾驶员", httpMethod = "POST")
	@PostMapping("/create")
	public ApiResponse create(@RequestBody DriverCreateInfo driverCreateInfo) {
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		try {
			log.info("新增驾驶员请求参数：{}，操作人：{}", JSONArray.toJSON(driverCreateInfo).toString(),loginUser.getUser().getPhonenumber());
			driverCreateInfo.setOptUserId(loginUser.getUser().getUserId());
			driverCreateInfo.setCompanyId(loginUser.getUser().getOwnerCompany());
			boolean createDriver = driverInfoService.createDriver(driverCreateInfo);
			if(createDriver){
				return ApiResponse.success();
			}else{
				return ApiResponse.error();
			}
		} catch (Exception e) {
			log.info("新增驾驶员请求参数：{}，操作人：{}", JSONArray.toJSON(driverCreateInfo).toString(),loginUser.getUser().getPhonenumber(),e);
			return ApiResponse.error("新增驾驶员失败");
		}
	}

	@Log(title = "驾驶员管理:驾驶员列表", businessType = BusinessType.OTHER)
	@ApiOperation(value = "driverList", notes = "驾驶员列表", httpMethod = "POST")
	@PostMapping("/driverList")
	public ApiResponse<PageResult<DriverQueryResult>> driverList(@RequestBody DriverQuery driverQuery) {
		List<DriverQueryResult> list = driverInfoService.queryDriverList(driverQuery);
		Integer totalNum = driverInfoService.queryDriverListCount(driverQuery);
		PageResult<DriverQueryResult> pageResult = new PageResult<DriverQueryResult>(Long.valueOf(totalNum), list);
		return ApiResponse.success(pageResult);
	}

	@Log(title = "驾驶员管理:驾驶员详情", businessType = BusinessType.OTHER)
	@ApiOperation(value = "detail", notes = "驾驶员详情", httpMethod = "POST")
	@PostMapping("/detail")
	public ApiResponse<DriverQueryResult> detail(@RequestBody Long driverId) {
		try {
			return ApiResponse.success(driverInfoService.queryDriverDetail(driverId));
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("查询驾驶员详情失败");
		}
	}

	@Log(title = "驾驶员管理:驾驶员启用/禁用", businessType = BusinessType.UPDATE)
	@ApiOperation(value = "optDriver", notes = "驾驶员启用/禁用", httpMethod = "POST")
	@PostMapping("/optDriver")
	public ApiResponse optRegime(String driverId,String state) {
		int updateDriverStatus = driverInfoService.updateDriverStatus(Long.valueOf(driverId), state);
		if(updateDriverStatus>0){
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}


	@Log(title = "驾驶员管理:指定车队下的可用驾驶员", businessType = BusinessType.OTHER)
	@ApiOperation(value = "carGroup", notes = "指定车队下的可用驾驶员", httpMethod = "POST")
	@PostMapping("/carGroup")
	public ApiResponse<CarGroupDriverInfo> queryCarGroupDriverList(@RequestBody Map map) {
		return ApiResponse.success(driverInfoService.queryCarGroupDriverList(map));
	}

	@Log(title = "驾驶员管理:校验驾驶员手机号是否已经存在", businessType = BusinessType.OTHER)
	@ApiOperation(value = "checkMobile", notes = "校验驾驶员手机号是否已经存在", httpMethod = "POST")
	@PostMapping("/checkMobile")
	public ApiResponse checkMobile(@RequestBody String mobile) {
		ApiResponse checkMobile = driverInfoService.checkMobile(mobile);
		return checkMobile;
	}

	@Log(title = "驾驶员管理:校验驾驶员用户的工号", businessType = BusinessType.OTHER)
	@ApiOperation(value = "checkjobNumber", notes = "校验驾驶员用户的工号", httpMethod = "POST")
	@PostMapping("/checkjobNumber")
	public ApiResponse checkjobNumber(@RequestBody DriverUserJobNumber driverUserJobNumber) {
		try {
			driverInfoService.checkjobNumber(driverUserJobNumber);
			return ApiResponse.success();
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error(e.getMessage());
		}
	}
}
