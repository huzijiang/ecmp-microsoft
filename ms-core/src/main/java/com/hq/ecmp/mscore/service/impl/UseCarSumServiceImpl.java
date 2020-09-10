package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.mapper.StatisticsForAdminMapper;
import com.hq.ecmp.mscore.mapper.UseCarSumMapper;
import com.hq.ecmp.mscore.service.UseCarSumService;
import com.hq.ecmp.mscore.vo.StatisticsForAdminVo;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * UseCarSumServiceImpl：
 *
 * @author: ll
 * @date: 2020/9/4 16:11
 */
@Service
public class UseCarSumServiceImpl implements UseCarSumService {
    private static final Logger log = LoggerFactory.getLogger(UseCarSumServiceImpl.class);

    @Autowired
    private UseCarSumMapper useCarSumMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StatisticsForAdminMapper statisticsForAdminMapper;

    @Override
    public List<UseCarSumExportVo> export(UseCarSumVo useCarSumVo) {
        List<UseCarSumExportVo> list = useCarSumMapper.getUseCarSumExportVoList(useCarSumVo);
//        List<UseCarSumExportVo> list = new ArrayList<>();
//        UseCarSumExportVo useCarSumExportVo = new UseCarSumExportVo();
//        useCarSumExportVo.setAmount("9999");
//        useCarSumExportVo.setAmountByIn("88");
//        useCarSumExportVo.setUseTimes("4");
//        useCarSumExportVo.setDeptName("AAA");
//        useCarSumExportVo.setOrders(99);
//        useCarSumExportVo.setUseTimes("999");
//
//        list.add(useCarSumExportVo);
        return list;
    }
    @Override
    public List<StatisticsForAdminVo> getData(StatisticsForAdmin statisticsForAdmin){
        List<StatisticsForAdminVo> list = new ArrayList<>();
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if(loginUser == null){
            log.warn("数据导出用户未登陆");
            return list;
        }
        Long companyId = loginUser.getUser().getOwnerCompany();
        statisticsForAdmin.setCompanyId(companyId);
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
//            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> unitVehicleByIn = statisticsForAdminMapper.unitVehicleByIn(statisticsForAdmin);
            for(StatisticsForAdminVo statisticsForAdminVo : unitVehicleByIn){
                list.add(statisticsForAdminVo);
            }
//            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> unitVehicleByOut = statisticsForAdminMapper.unitVehicleByOut(statisticsForAdmin);
            for(StatisticsForAdminVo statistics : list){
                for(StatisticsForAdminVo statisticsForAdminVo : unitVehicleByOut){
                    if(null != statisticsForAdminVo && statistics.getDeptName().equals(statisticsForAdminVo.getDeptName())){
                        statistics.setAmountByOut(statisticsForAdminVo.getAmountByOut());
                        statistics.setUseTimesByOut(statisticsForAdminVo.getUseTimesByOut());
                        statistics.setOrdersByOut(statisticsForAdminVo.getOrdersByOut());
                    }
                }
            }
        }catch (Exception e){
            log.error("数据导出查询数据异常",e);
        }
        return list;
    }
}
