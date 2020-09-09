package com.hq.ecmp.ms.api.controller.export;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.ms.api.controller.statistics.UseCarStatisticsExportController;
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
        UseCarSumVo useCarStatisticsParamDTO = new UseCarSumVo();
        useCarStatisticsParamDTO.setBeginDate(DateUtils.strToDate("2020-08-10 14:53:32",DateUtils.YYYY_MM_DD_HH_MM_SS));
        useCarStatisticsParamDTO.setEndDate(DateUtils.strToDate("2020-09-9 14:53:32",DateUtils.YYYY_MM_DD_HH_MM_SS));
        useCarStatisticsExportController.export(useCarStatisticsParamDTO);
    }

}