package com.hq.ecmp.ms.api.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.service.UseCarSumService;
import com.hq.ecmp.mscore.vo.StatisticsForAdminVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UseCarSumController：
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
    @PostMapping("/list")
    public ApiResponse<List<StatisticsForAdminVo>>  export(@RequestHeader(value = "Authorization",required = false) String token,StatisticsForAdmin statisticsParam) {
        log.info("导出用车数据参数==========={}", JSONObject.toJSONString(statisticsParam));
        return ApiResponse.success(useCarSumService.getData(statisticsParam));
    }
}
