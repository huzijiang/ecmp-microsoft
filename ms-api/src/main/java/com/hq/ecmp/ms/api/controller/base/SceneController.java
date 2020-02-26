package com.hq.ecmp.ms.api.controller.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.service.ISceneInfoService;

import io.swagger.annotations.ApiOperation;

/**
 * 用户用车场景
 * @author cm
 *
 */
@RestController
@RequestMapping("/scene")
public class SceneController {
	@Autowired
	private ISceneInfoService sceneInfoService;
	@Autowired
    private TokenService tokenService;
	
	/**
	 * 获取用户的用车场景
	 * @param userDto
	 * @return
	 */
	@ApiOperation(value = "getAll", notes = "获取用车场景", httpMethod = "POST")
	@PostMapping("/getAll")
	public ApiResponse<List<SceneInfo>> getAllScene(@RequestBody UserDto userDto) {
		//获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
		List<SceneInfo> selectAllSceneSort = sceneInfoService.selectAllSceneSort(loginUser.getUser().getUserId());
		return ApiResponse.success(selectAllSceneSort);
	}
}
