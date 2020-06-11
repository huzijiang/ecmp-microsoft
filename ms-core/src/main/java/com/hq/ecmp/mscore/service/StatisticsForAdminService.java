package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;

public interface StatisticsForAdminService {

    ApiResponse driverOutranking(StatisticsForAdmin statisticsForAdmin);

    ApiResponse vehicleExpenses(StatisticsForAdmin statisticsForAdmin);

    ApiResponse unitVehicle(StatisticsForAdmin statisticsForAdmin);

    ApiResponse useOfMechanismVehicles(StatisticsForAdmin statisticsForAdmin);

    ApiResponse driverOut(StatisticsForAdmin statisticsForAdmin);

    ApiResponse appointmentTime(StatisticsForAdmin statisticsForAdmin);

    ApiResponse modelUse(StatisticsForAdmin statisticsForAdmin);

    ApiResponse leasing(StatisticsForAdmin statisticsForAdmin);

    ApiResponse details(StatisticsForAdmin statisticsForAdmin);
}
