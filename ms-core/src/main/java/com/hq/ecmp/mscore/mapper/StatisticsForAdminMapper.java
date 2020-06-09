package com.hq.ecmp.mscore.mapper;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.vo.StatisticsForAdminVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsForAdminMapper {

    //出车次数排行
    List<StatisticsForAdminVo> driverOutranking(StatisticsForAdmin statisticsForAdmin);
    //用车费用排行
    List<StatisticsForAdminVo> vehicleExpenses(StatisticsForAdmin statisticsForAdmin);
    //单位用车统计内部
    List<StatisticsForAdminVo> unitVehicleByIn(StatisticsForAdmin statisticsForAdmin);
    //单位用车统计外部
    List<StatisticsForAdminVo> unitVehicleByOut(StatisticsForAdmin statisticsForAdmin);
    //机关车辆使用统计
    List<StatisticsForAdminVo> useOfMechanismVehicles(StatisticsForAdmin statisticsForAdmin);

    List<StatisticsForAdminVo> driverOut(StatisticsForAdmin statisticsForAdmin);

    List<StatisticsForAdminVo> appointmentTime(StatisticsForAdmin statisticsForAdmin);

    List<StatisticsForAdminVo> modelUse(StatisticsForAdmin statisticsForAdmin);

    List<StatisticsForAdminVo> leasing(StatisticsForAdmin statisticsForAdmin);

    List<StatisticsForAdminVo> details(StatisticsForAdmin statisticsForAdmin);
}
