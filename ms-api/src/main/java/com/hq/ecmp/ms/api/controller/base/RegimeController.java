package com.hq.ecmp.ms.api.controller.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.ecmp.mscore.dto.RegimenDTO;
import com.hq.ecmp.mscore.vo.RegimenVO;
import com.hq.ecmp.mscore.vo.SceneRegimensVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeLimitUseCarCityInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.vo.PageResult;


import io.swagger.annotations.ApiOperation;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 11:59
 */
@Slf4j
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
    @Log(title = "用车制度",content = "查询登录用户的用车制度", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getUserRegimes",notes = "根据用户信息查询用户的用车制度信息",httpMethod ="POST")
    @PostMapping("/getUserRegimes")
    public ApiResponse<List<RegimenVO>> getUserRegimes(@RequestBody(required = false)UserDto userDto){
        try {
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
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
    }


    /**
     * 根据用户信息查询用户的用车制度信息（可添加场景id条件筛选）
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @Log(title = "用车制度",content = "查询登录用户场景与制度", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getUserScenesRegimes",notes = "根据用户信息查询用户的用车场景与制度信息",httpMethod ="POST")
    @PostMapping("/getUserScenesRegimes")
    public ApiResponse<List<SceneRegimensVo>> getUserScenesRegimes(){
        //查询登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        if(userId == null){
            return ApiResponse.error("该用户不是公司员工");
        }
        try {
            //根据用户id查询用车制度
            List<SceneRegimensVo> list = regimeInfoService.getUserScenesRegimes(userId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            log.error("查询用户场景与制度列表失败，登录用户：{}",userId,e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     *
     * 根据场景id查询用车制度集合
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @Log(title = "用车制度",content = "根据场景id查询制度集合", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getRegimesBySceneId",notes = "根据用车场景查询用车制度",httpMethod ="POST")
    @PostMapping("/getRegimesBySceneId")
    public ApiResponse<List<RegimenVO>> getRegimesBySceneId(@RequestBody UserDto userDto){
        List<RegimenVO> all = regimeInfoService.selectRegimesBySceneId(userDto.getSceneId());
        return ApiResponse.success(all);
    }


    /**
     *
     * 根据场景id集合查询用车制度集合
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @Log(title = "用车制度",content = "根据场景id查询制度集合", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getRegimesBySceneIds",notes = "根据用车场景集合用车制度集合",httpMethod ="POST")
    @PostMapping("/getRegimesBySceneIds")
    public ApiResponse<List<RegimenVO>> getRegimesBySceneIds(@RequestBody UserDto userDto){
        if(userDto.getAll()){
            //如果为真， 查询全部用车制度
            List<RegimenVO> allRegimen = regimeInfoService.selectAllRegimenVO();
            return ApiResponse.success(allRegimen);
        }
        String sceneIds = userDto.getSceneIds();
        String[] sceneIdStrs = sceneIds.split(",");
        List all = new ArrayList<>();
        for (String sceneIdStr : sceneIdStrs) {
            if(StringUtils.isNotEmpty(sceneIdStr)){
                List<RegimenVO> regimenVOS = regimeInfoService.selectRegimesBySceneId(Long.valueOf(sceneIdStr));
                if(CollectionUtils.isNotEmpty(regimenVOS)){
                    all.addAll(regimenVOS);
                }
            }
        }
        return ApiResponse.success(all);
    }

    /**
     *
     * 查询所有的用车制度信息
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息列表
     */
    @Log(title = "用车制度",content = "查询所有用车制度，不带分页", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getAllRegimes",notes = "查询所有的用车制度信息",httpMethod ="POST")
    @PostMapping("/getAllRegimes")
    public ApiResponse<List<RegimeInfo>> getAllRegimes(){
        List<RegimeInfo> all = regimeInfoService.selectAll();
        return ApiResponse.success(all);
    }



    /**
     *
     * 查询用户用车制度可用网约车型
     * @param
     * @return
     */
    @Log(title = "用车制度",content = "可用网约车型", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getUserOnlineCarLevels",notes = "查询用户可用网约车型等级",httpMethod ="POST")
    @PostMapping("/getUserOnlineCarLevels")
    public ApiResponse<String> getUserOnlineCarLevels(@RequestBody RegimenDTO regimenDTO){
        String result = regimeInfoService.getUserOnlineCarLevels(regimenDTO.getRegimenId(),regimenDTO.getType());
        if(ObjectUtils.isEmpty(result)){
            return ApiResponse.error("查无数据");
        }
        return ApiResponse.success("查询成功",result);
    }

    /**
     * 通过用车制度编号,查询用车制度的详细信息
     * @param regimeDto regimeDto
     * @return
     */
    @Log(title = "用车制度",content = "用车制度详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getRegimeInfo",notes = "通过用车制度编号,查询用车制度的详细信息",httpMethod ="POST")
    @PostMapping("/getRegimeInfo")
    public ApiResponse<RegimeVo> getRegimeInfo(@RequestBody RegimeDto regimeDto){
        try {
            RegimeVo regimeVo = regimeInfoService.selectRegimeDetailById(regimeDto.getRegimenId());
            return ApiResponse.success(regimeVo);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("查询失败");
        }

    }

    @Log(title = "用车制度:可用城市or不可用城市", businessType = BusinessType.OTHER)
    @ApiOperation(value = "queryRegimeCityLimit",notes = "通过用车制度编号,查询用车制度的可用or不可用车城市",httpMethod ="POST")
    @PostMapping("/queryRegimeCityLimit")
    public ApiResponse<RegimeLimitUseCarCityInfo> queryRegimeCityLimit(@RequestBody Long regimeId){
        try {
            return ApiResponse.success(regimeInfoService.queryRegimeCityLimit(regimeId));
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("查询公务制度可用城市or不可用城市失败");
        }

    }


    @Log(title = "用车制度:创建用车制度", businessType = BusinessType.INSERT)
	@ApiOperation(value = "createRegime", notes = "创建用车制度", httpMethod = "POST")
	@PostMapping("/createRegime")
	public ApiResponse createRegime(@RequestBody RegimePo regimePo) {
		 HttpServletRequest request = ServletUtils.getRequest();
	      LoginUser loginUser = tokenService.getLoginUser(request);
	      regimePo.setOptId(loginUser.getUser().getUserId());
	      regimePo.setCompanyId(loginUser.getUser().getOwnerCompany());
		boolean createRegime = regimeInfoService.createRegime(regimePo);
		if (createRegime) {
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}

    @Log(title = "用车制度:修改用车制度", businessType = BusinessType.UPDATE)
	@ApiOperation(value = "updateRegime", notes = "修改用车制度", httpMethod = "POST")
	@PostMapping("/updateRegime")
	public ApiResponse updateRegime(@RequestBody RegimePo regimePo) {
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		regimePo.setOptId(loginUser.getUser().getUserId());
        regimePo.setCompanyId(loginUser.getUser().getOwnerCompany());
		boolean updateRegime = regimeInfoService.updateRegime(regimePo);
		if (updateRegime) {
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}


    @Log(title = "用车制度:查询制度列表", businessType = BusinessType.OTHER)
	@ApiOperation(value = "queryRegimeList", notes = "查询制度列表", httpMethod = "POST")
	@PostMapping("/queryRegimeList")
	public ApiResponse<PageResult<RegimeVo>> queryRegimeList(@RequestBody RegimeQueryPo regimeQueryPo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        regimeQueryPo.setCompanyId(loginUser.getUser().getOwnerCompany());
        List<RegimeVo> regimeVoList = regimeInfoService.queryRegimeList(regimeQueryPo);
		//总条数
		Integer count = regimeInfoService.queryRegimeListCount(regimeQueryPo);
		PageResult<RegimeVo> pageResult = new PageResult<RegimeVo>(Long.valueOf(count), regimeVoList);
		return ApiResponse.success(pageResult);
	}

    @Log(title = "用车制度:制度删除 or启用or停用", businessType = BusinessType.OTHER)
	@ApiOperation(value = "optRegime", notes = "制度删除 or启用or停用", httpMethod = "POST")
	@PostMapping("/optRegime")
	public ApiResponse optRegime(@RequestBody RegimeOpt regimeOpt) {
		 //查询登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        regimeOpt.setOptUserId(loginUser.getUser().getUserId());
		boolean opt = regimeInfoService.optRegime(regimeOpt);
		if(opt){
			return ApiResponse.success();
		}
		return ApiResponse.error();
	}

    @Log(title = "用车制度:制度详情查询", businessType = BusinessType.OTHER)
	@ApiOperation(value = "detail", notes = "后管制度详情查询", httpMethod = "POST")
	@PostMapping("/detail")
	public ApiResponse<RegimeVo> queryRegimeDetail(@RequestBody Long regimeId) {
		return ApiResponse.success(regimeInfoService.queryRegimeDetail(regimeId));
	}

    /**
     * 通过申请人查询可用的制度
     * @param
     * @return ApiResponse<List<RegimeInfo>> 用车制度信息
     */
    @Log(title = "用车制度",content = "通过申请人查询可用的制度", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getUserSystem",notes = "通过申请人查询可用的制度",httpMethod ="POST")
    @PostMapping("/getUserSystem")
    public ApiResponse<List<RegimenVO>> getUserSystem(@RequestBody Long userId){
        try {
            if(userId==null){
                return ApiResponse.error("该用户不是公司员工");
            }
            //根据用户id查询用车制度
            List<RegimenVO> list = regimeInfoService.getUserSystem(userId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            log.error("查询用户场景与制度列表失败，用户id：{}",userId,e);
            return ApiResponse.error(e.getMessage());
        }
    }


}
