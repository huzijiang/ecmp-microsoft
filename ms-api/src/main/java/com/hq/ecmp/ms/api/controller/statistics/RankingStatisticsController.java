package com.hq.ecmp.ms.api.controller.statistics;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics/ranking")
public class RankingStatisticsController {
    @Autowired
    private StatisticsRankingService statisticsRankingService;

    @ApiOperation(value = "ranking",notes = "排行榜",httpMethod = "POST")
    @PostMapping("/ranking")
    public ApiResponse ranking(@RequestHeader("Authorization") String authorization,@RequestBody StatisticsParam statisticsParam){
        try {
            return statisticsRankingService.ranking(statisticsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }
}
