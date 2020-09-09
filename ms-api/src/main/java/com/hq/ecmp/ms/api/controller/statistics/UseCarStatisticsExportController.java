package com.hq.ecmp.ms.api.controller.statistics;

import com.hq.api.monitor.domain.SysOperLog;
import com.hq.api.monitor.domain.vo.SysOperLogVo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.poi.ExcelUtil;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.web.domain.AjaxResult;
import com.hq.ecmp.mscore.service.UseCarSumService;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UseCarSumController：
 *
 * @author: ll
 * @date: 2020/9/4 15:59
 */
@Slf4j
@RestController
@RequestMapping("/useCar")
@Api(tags = {"报表"})
public class UseCarStatisticsExportController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UseCarSumService useCarSumService;

    @Log(title = "用车统计",content = "导出用车数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ApiResponse<List<UseCarSumExportVo>>  export(UseCarSumVo useCarSumVo) {
        log.info("导出用车数据===========");
        return ApiResponse.success(useCarSumService.export(useCarSumVo));
    }
}
