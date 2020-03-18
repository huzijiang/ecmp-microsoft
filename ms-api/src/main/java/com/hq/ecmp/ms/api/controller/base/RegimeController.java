package com.hq.ecmp.ms.api.controller.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.ecmp.mscore.vo.RegimenVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.SceneListVO;

import io.swagger.annotations.ApiOperation;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 11:59
 */
@RestController
@RequestMapping("/regime")
public class RegimeController {

    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private TokenService tokenService;


    /**
     * 根据用户信息查询用户的用车制度信息（可添加场景id条件筛选）
     * @param userDto userDto
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getUserRegimes",notes = "根据用户信息查询用户的用车制度信息",httpMethod ="POST")
    @PostMapping("/getUserRegimes")
    public ApiResponse<List<RegimenVO>> getUserRegimes(@RequestBody(required = false)UserDto userDto){
        //查询登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //初始化sceneId
        Long sceneId = null;
        if(userDto != null){
            sceneId = userDto.getSceneId();
        }
        //根据用户id查询用车制度
        List<RegimenVO> regimeInfoList = regimeInfoService.findRegimeInfoListByUserId(loginUser.getUser().getUserId(),sceneId);
        if(CollectionUtils.isEmpty(regimeInfoList)){
            return ApiResponse.error("暂无数据");
        }
        return ApiResponse.success(regimeInfoList);
    }

    /**
     *
     * 查询所有的用车制度信息
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getAllRegimes",notes = "查询所有的用车制度信息",httpMethod ="POST")
    @PostMapping("/getAllRegimes")
    public ApiResponse<List<RegimeInfo>> getAllRegimes(){
        List<RegimeInfo> all = regimeInfoService.selectAll();
        return ApiResponse.success(all);
    }

    /**
     * 通过用车制度编号,查询用车制度的详细信息
     * @param regimeDto regimeDto
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @ApiOperation(value = "getRegimeInfo",notes = "通过用车制度编号,查询用车制度的详细信息",httpMethod ="POST")
    @PostMapping("/getRegimeInfo")
    public ApiResponse<RegimeInfo> getRegimeInfo(@RequestBody RegimeDto regimeDto){
        RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(regimeDto.getRegimenId());
        if(regimeInfo == null){
            return ApiResponse.error("暂无数据");
        }else {
            return ApiResponse.success(regimeInfo);
        }

    }
    
    
	@ApiOperation(value = "createRegime", notes = "创建用车制度", httpMethod = "POST")
	@PostMapping("/createRegime")
	public ApiResponse createRegime(@RequestBody RegimePo regimePo) {
		boolean createRegime = regimeInfoService.createRegime(regimePo);
		if (createRegime) {
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}
	
	@ApiOperation(value = "queryRegimeList", notes = "查询制度列表", httpMethod = "POST")
	@PostMapping("/queryRegimeList")
	public ApiResponse<PageResult<RegimeVo>> queryRegimeList(@RequestBody RegimeQueryPo regimeQueryPo) {
		List<RegimeVo> regimeVoList = regimeInfoService.queryRegimeList(regimeQueryPo);
		//总条数
		Integer count = regimeInfoService.queryRegimeListCount(regimeQueryPo);
		PageResult<RegimeVo> pageResult = new PageResult<RegimeVo>(Long.valueOf(count), regimeVoList);
		return ApiResponse.success(pageResult);
	}
    
	@ApiOperation(value = "optRegime", notes = "制度删除 or启用or停用", httpMethod = "POST")
	@PostMapping("/optRegime")
	public ApiResponse optRegime(@RequestBody RegimeOpt regimeOpt) {
		boolean opt = regimeInfoService.optRegime(regimeOpt);
		if(opt){
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}
	
	
	@ApiOperation(value = "detail", notes = "后管制度详情查询", httpMethod = "POST")
	@PostMapping("/detail")
	public ApiResponse<RegimeVo> queryRegimeDetail(@RequestBody Long regimeId) {
		return ApiResponse.success(regimeInfoService.queryRegimeDetail(regimeId));
	}
	
	
}
