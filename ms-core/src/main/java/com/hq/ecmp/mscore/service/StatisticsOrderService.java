package com.hq.ecmp.mscore.service;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;

public interface StatisticsOrderService {
	public ApiResponse orderEndOfCarModelOfServiceType(StatisticsParam statisticsParam);
	public ApiResponse orderEndOfTimeInterval(StatisticsParam statisticsParam);
	public ApiResponse orderEndOfScene(StatisticsParam statisticsParam);
	public ApiResponse orderEndOfCity(StatisticsParam statisticsParam);
	public ApiResponse orderEndOfDept(StatisticsParam statisticsParam);
}
