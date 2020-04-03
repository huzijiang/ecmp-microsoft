package com.hq.ecmp.ms.api.controller.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.SceneDTO;
import com.hq.ecmp.mscore.dto.SceneSortDTO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.SceneDetailVO;
import com.hq.ecmp.mscore.vo.SceneListVO;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * @param
	 * @return
	 */
	@ApiOperation(value = "getAll", notes = "获取用车场景", httpMethod ="GET")
	@GetMapping("/getAll")
	public ApiResponse<List<SceneInfo>> getAllScene() {
		//获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
		List<SceneInfo> selectAllSceneSort = sceneInfoService.selectAllSceneSort(loginUser.getUser().getUserId());
		return ApiResponse.success(selectAllSceneSort);
	}

	/**
	 * 创建用车场景
	 * @param
	 * @return
	 */
	@ApiOperation(value = "saveScene", notes = "创建用车场景", httpMethod ="POST")
	@RequestMapping("/saveScene")
	public ApiResponse saveScene(@RequestBody SceneDTO sceneDTO) {
		//获取登录用户
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		try {
			sceneInfoService.saveScene(sceneDTO,userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.success("保存失败");
		}
		return ApiResponse.success("保存用车场景成功");
	}

	/**
	 * 删除用车场景
	 * @param
	 * @return
	 */
	@ApiOperation(value = "deleteScene", notes = "删除用车场景", httpMethod ="POST")
	@RequestMapping("/deleteScene")
	public ApiResponse deleteScene(@RequestBody Long sceneId) {
		try {
			SceneDTO sceneDTO = new SceneDTO();
			sceneDTO.setSceneId(sceneId);
			int i = sceneInfoService.deleteSceneInfoById(sceneDTO.getSceneId());
			if(i == 1){
				return ApiResponse.success("删除成功");
			}else {
				return ApiResponse.error("删除场景失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.success("删除失败");
		}
	}

	/**
	 * 修改用车场景
	 * @param
	 * @return
	 */
	@ApiOperation(value = "updateScene", notes = "修改用车场景", httpMethod ="POST")
	@PostMapping("/updateScene")
	public ApiResponse updateScene(@RequestBody SceneDTO sceneDTO) {
		//获取登录用户
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		try {
			sceneInfoService.updateScene(sceneDTO,userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.success("修改场景失败");
		}
		return ApiResponse.success("修改用车场景成功");
	}

	/**
	 * 用车场景详情
	 * @param
	 * @return
	 */
	@ApiOperation(value = "getSceneDetail", notes = "查询用车场景详情", httpMethod ="POST")
	@PostMapping("/getSceneDetail")
	public ApiResponse<SceneDetailVO> getSceneDetail(@RequestBody Long sceneId) {
		try {
			SceneDTO sceneDTO = new SceneDTO();
			sceneDTO.setSceneId(sceneId);
			//查询场景详情
			SceneDetailVO sceneDetailVO = sceneInfoService.selectSceneDetail(sceneDTO);
			if(ObjectUtils.isEmpty(sceneDetailVO)){
				return ApiResponse.error("暂无数据");
			}else {
				return ApiResponse.success(sceneDetailVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("查询场景详情失败");
		}
	}

	/**
	 * 分页查询用车场景列表信息（带搜索功能）
	 * @param
	 * @return
	 */
	@ApiOperation(value = "getSceneList", notes = "查询用车场景列表信息", httpMethod ="POST")
	@PostMapping("/getSceneList")
	public ApiResponse<PageResult<SceneListVO>> getSceneList(@RequestBody PageRequest pageRequest) {
		try {
			PageResult<SceneListVO> list = sceneInfoService.seleSceneByPage(pageRequest);
			return ApiResponse.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("查询场景列列表信息失败");
		}
	}

	/**
	 * 场景排序 上移/下移
	 * @param
	 * @return
	 */
	@ApiOperation(value = "sortScene", notes = "场景排序 上移/下移", httpMethod ="POST")
	@PostMapping("/sortScene")
	public ApiResponse sortScene(@RequestBody SceneSortDTO sceneSortDTO) {
		//获取登录用户
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		try {
			sceneInfoService.sortScene(sceneSortDTO,userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.error("修改场景顺序失败");
		}
		return ApiResponse.success("修改场景顺序成功");
	}


}
