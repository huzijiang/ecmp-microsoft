package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;

public interface StatisticsAbilityService {
    public ApiResponse getCarGroupTreeByDeptIds(StatisticsParam statisticsParam);
    public ApiResponse driverSum(StatisticsParam statisticsParam);
    public ApiResponse driverSumByDate(StatisticsParam statisticsParam);
    public ApiResponse driverSumByDimension(StatisticsParam statisticsParam);
    public ApiResponse carSum(StatisticsParam statisticsParam);
    public ApiResponse carSumByDate(StatisticsParam statisticsParam);
    public ApiResponse carSumByByDimension(StatisticsParam statisticsParam);
    public ApiResponse dispatchSum(StatisticsParam statisticsParam);
    public ApiResponse dispatchSumByDate(StatisticsParam statisticsParam);

}
