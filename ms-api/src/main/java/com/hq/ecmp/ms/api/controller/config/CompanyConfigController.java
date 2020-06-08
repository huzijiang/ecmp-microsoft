package com.hq.ecmp.ms.api.controller.config;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.config.ConfigInfoDTO;
import com.hq.ecmp.mscore.dto.config.EnterPriseBaseInfoDTO;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * @author xueyong
 */
@RestController
@RequestMapping("/config/")
@Slf4j
public class CompanyConfigController {

    @Autowired
    private IEcmpConfigService ecmpConfigService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取企业配置信息
     */
    @ApiOperation(value = "queryCompanyInfo ", notes = "获取企业配置信息")
    @PostMapping("/queryCompanyInfo")
    public ApiResponse<ConfigInfoDTO> query() {
        try {
            ConfigInfoDTO configInfoDTO = ecmpConfigService.selectConfigInfo(getCurrentUserCompany());
            return ApiResponse.success(configInfoDTO);
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return null;
    }

    /**
     * 设置企业基本信息
     */
    @ApiOperation(value = "companyInfo ", notes = "设置企业信息")
    @PostMapping("/companyInfo")
    public ApiResponse companyInfo(EnterPriseBaseInfoDTO enterPriseBaseInfoDTO) {
        enterPriseBaseInfoDTO.setCompanyId(getCurrentUserCompany());
        ecmpConfigService.setUpBaseInfo(enterPriseBaseInfoDTO);
        return ApiResponse.success();
    }

    /**
     * 设置开屏图信息
     */
    @ApiOperation(value = "screen ", notes = "设置开屏图片信息")
    @PostMapping(value = "/screen", produces = "application/json;charset=UTF-8")
    public ApiResponse screen(@RequestParam("file") MultipartFile file, String status, String value) {
        ecmpConfigService.setUpWelComeImage(status, value, file,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置背景图信息
     */
    @ApiOperation(value = "backgroundImage ", notes = "设置背景图片信息")
    @PostMapping("/backgroundImage")
    public ApiResponse backgroundImage(MultipartFile file, String status, String value) {
        ApiResponse apiResponse = ecmpConfigService.setUpBackGroundImage(status, value, file,getCurrentUserCompany());
        return apiResponse;
    }

    /**
     * 设置短信信息
     */
    @ApiOperation(value = "sms ", notes = "设置短信开关")
    @PostMapping("/sms")
    public ApiResponse sms(String status) {
        ecmpConfigService.setUpSms(status,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置虚拟小号信息
     */
    @ApiOperation(value = "virtualPhone ", notes = "设置虚拟小号信息")
    @PostMapping("/virtualPhone")
    public ApiResponse virtualPhone(String status) {
        ecmpConfigService.setUpVirtualPhone(status,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置确认订单方式
     *
     * @param rideHailing 网约车类型
     * @param owenType    自有车类型
     */
    @ApiOperation(value = "confirmOrder ", notes = "设置确认订单方式")
    @PostMapping("/confirmOrder")
    public ApiResponse confirmOrder(String status, String value, String owenType, String rideHailing) {
        ecmpConfigService.setUpOrderConfirm(status, value, owenType, rideHailing,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置派单方式
     *
     * @param value JSON格式
     */
    @ApiOperation(value = "dispatch ", notes = "设置派单方式")
    @PostMapping("/dispatch")
    public ApiResponse dispatch(String status, String value) {
        ecmpConfigService.setUpDispatchInfo(status, value,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置往返等待时长
     *
     * @param value 开状态下的时长数值
     */
    @ApiOperation(value = "waitDuration ", notes = "设置往返等待时长")
    @PostMapping("/waitDuration")
    public ApiResponse waitDuration(String status, String value) {
        ecmpConfigService.setUpWaitMaxMinute(status, value,getCurrentUserCompany());
        return ApiResponse.success();
    }

    /**
     * 设置后台公告开关
     * @param status 开关状态
     */
    @ApiOperation(value = "message ", notes = "设置后台公告开关")
    @PostMapping("/message")
    public ApiResponse message(String status) {
        ecmpConfigService.setUpMessageConfig(status,getCurrentUserCompany());
        return ApiResponse.success();
    }

    private Long getCurrentUserCompany(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        return loginUser.getUser().getDept().getCompanyId();
    }
    /**
     * 获取企业配置信息
     */
    @ApiOperation(value = "queryAppCompanyInfo ", notes = "获取企业配置信息")
    @PostMapping("/queryAppCompanyInfo")
    public ApiResponse<ConfigInfoDTO> queryAppCompanyInfo() {
        HttpServletRequest request = ServletUtils.getRequest();
        //针对目前只有一家 而且每家都自己一个数据库  先写死
        Long companyId = 100L;
        ConfigInfoDTO configInfoDTO = ecmpConfigService.selectConfigInfo(companyId);
        return ApiResponse.success(configInfoDTO);
    }
}
