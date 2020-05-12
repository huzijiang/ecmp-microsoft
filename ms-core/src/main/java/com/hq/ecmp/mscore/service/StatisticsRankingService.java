package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;

public interface StatisticsRankingService {
    public ApiResponse ranking(StatisticsParam statisticsParam);
}
