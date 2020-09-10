package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.vo.StatisticsForAdminVo;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;

import java.util.List;

/**
 * UseCarSumService：
 *
 * @author: ll
 * @date: 2020/9/4 16:10
 */
public interface UseCarSumService {
    List<UseCarSumExportVo> export(UseCarSumVo useCarSumVo);
    List<StatisticsForAdminVo> getData(StatisticsForAdmin statisticsForAdmin);
}
