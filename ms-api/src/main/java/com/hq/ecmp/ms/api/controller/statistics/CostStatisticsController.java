package com.hq.ecmp.ms.api.controller.statistics;


import com.hq.api.system.service.ISysDeptService;
import com.hq.common.core.api.ApiResponse;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.StatisticsCostService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/statistics/cost")
public class CostStatisticsController {
    @Autowired
    private StatisticsCostService statisticsCostService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISysDeptService iSysDeptService;

    @ApiOperation(value = "cost",notes = "公司部门维度成本分析",httpMethod = "POST")
    @PostMapping("/cost")
    public ApiResponse cost(@RequestHeader("Authorization") String authorization,@RequestBody StatisticsParam statisticsParam){
        try {
            //当前登录人所属公司id
            List<Long> longs = new ArrayList<>();
            longs.add(iSysDeptService.findCompany(tokenService.getLoginUser(authorization).getUser().getDept().getDeptId(),2).getDeptId());
            statisticsParam.setDeptIds(longs);
            return statisticsCostService.cost(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
}
