package com.hq.ecmp.ms.api.controller.config;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.config.ConfigInfoDTO;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * App开屏图和背景图
 */
@RestController
@RequestMapping("/config/app/")
@Slf4j
public class AppCompanyConfigController {

    @Autowired
    private IEcmpConfigService ecmpConfigService;

    @Autowired
    private TokenService tokenService;

    private Long getCurrentUserCompany(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        return loginUser.getUser().getOwnerCompany();
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
