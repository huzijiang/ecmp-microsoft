package com.hq.ecmp.ms.api.controller.export;

import com.hq.ecmp.ms.api.controller.statistics.UseCarStatisticsExportController;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.vo.UseCarSumVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * exportTest：
 *
 * @author: ll
 * @date: 2020/9/9 15:17
 */
@SpringBootTest
class exportTest {
    @Autowired
    private UseCarStatisticsExportController useCarStatisticsExportController;

    @Test
    void contextLoads() {
        StatisticsForAdmin statisticsParam = new StatisticsForAdmin();
        statisticsParam.setBeginDate("2020-08-10 14:53:32");
        statisticsParam.setEndDate("2020-08-10 14:53:32");
        useCarStatisticsExportController.export(statisticsParam);
    }

}