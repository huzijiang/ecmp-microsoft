package com.hq.ecmp.ms.api.controller.export;

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
        useCarStatisticsParamDTO.setDeptId(null);
        useCarStatisticsExportController.export(useCarStatisticsParamDTO);
    }

}