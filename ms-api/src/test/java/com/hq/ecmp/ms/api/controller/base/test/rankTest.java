package com.hq.ecmp.ms.api.controller.base.test;

import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectCarDto;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * rankTest：
 *
 * @author: ll
 * @date: 2020/9/21 13:15
 */
@SpringBootTest
@ActiveProfiles("test")
public class rankTest {
    @Autowired
    private StatisticsRankingService statisticsRankingService;
    @Test
    public void rankingTest(){
        StatisticsParam statisticsParam = new StatisticsParam();
        statisticsParam.setCompanyId(100l);
        statisticsParam.setType(2);
        statisticsRankingService.ranking(statisticsParam);
    }
}
