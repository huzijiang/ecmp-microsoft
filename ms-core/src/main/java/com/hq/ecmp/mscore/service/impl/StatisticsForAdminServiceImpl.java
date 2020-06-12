package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.mapper.StatisticsForAdminMapper;
import com.hq.ecmp.mscore.service.StatisticsForAdminService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.StatisticsForAdminDetailVo;
import com.hq.ecmp.mscore.vo.StatisticsForAdminVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ghb
 * @description 数据统计--管理员 实现层
 * @date 2020/6/9
 */

@Service
public class StatisticsForAdminServiceImpl implements StatisticsForAdminService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsForAdminServiceImpl.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StatisticsForAdminMapper statisticsForAdminMapper;

    //出车次数排行
    @Override
    public ApiResponse driverOutranking(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
            //出车次数排行
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> driverOutranking = statisticsForAdminMapper.driverOutranking(statisticsForAdmin);
            Long orders = 0L;
            for (StatisticsForAdminVo statisticsForAdminVo : driverOutranking){
                orders = orders+ statisticsForAdminVo.getOrders();
            }
            jsonObject.put("driverOutranking",driverOutranking);
            jsonObject.put("sumOrder",orders);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }


    //用车费用排行
    @Override
    public ApiResponse vehicleExpenses(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
            //用车费用排行
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> vehicleExpenses = statisticsForAdminMapper.vehicleExpenses(statisticsForAdmin);
            BigDecimal sumAmount = new BigDecimal(0);
            for (StatisticsForAdminVo statisticsForAdminVo : vehicleExpenses){
                sumAmount = sumAmount.add(statisticsForAdminVo.getAmount());
            }
            jsonObject.put("vehicleExpenses",vehicleExpenses);
            jsonObject.put("sumAmount",sumAmount);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }

    //单位用车统计
    @Override
    public ApiResponse unitVehicle(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        StatisticsForAdmin statisticsForAdminQuery = new StatisticsForAdmin();
        statisticsForAdminQuery.setCompanyId(companyId);
        statisticsForAdmin.setCompanyId(companyId);
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
        try {
            List<String> carUseDepts = statisticsForAdminMapper.getDeptNames(statisticsForAdminQuery);
            List<StatisticsForAdminVo> list = new ArrayList<>();
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> unitVehicleByIn = statisticsForAdminMapper.unitVehicleByIn(statisticsForAdmin);
            for(StatisticsForAdminVo statisticsForAdminVo : unitVehicleByIn){
                list.add(statisticsForAdminVo);
            }
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
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
            PageInfo<StatisticsForAdminVo> info = new PageInfo<>(list);
            jsonObject.put("carUseDepts",carUseDepts);
            jsonObject.put("list",new PageResult<>(info.getTotal(), info.getPages(), list));
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }


    //机关车辆使用统计
    @Override
    public ApiResponse useOfMechanismVehicles(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        StatisticsForAdmin statisticsForAdminQuery = new StatisticsForAdmin();
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        statisticsForAdminQuery.setCompanyId(companyId);
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        try {
            List<String> carLicenses = statisticsForAdminMapper.getCarLicenses(statisticsForAdminQuery);
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> useOfMechanismVehicles = statisticsForAdminMapper.useOfMechanismVehicles(statisticsForAdmin);
            PageInfo<StatisticsForAdminVo> info = new PageInfo<>(useOfMechanismVehicles);
            jsonObject.put("useOfMechanismVehicles",new PageResult<>(info.getTotal(), info.getPages(), useOfMechanismVehicles));
            jsonObject.put("carLicenses",carLicenses);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;

    }

    //驾驶员出车统计
    @Override
    public ApiResponse driverOut(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        StatisticsForAdmin statisticsForAdminQuery = new StatisticsForAdmin();
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        statisticsForAdminQuery.setCompanyId(companyId);
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
        try {
            List<String> driverNames = statisticsForAdminMapper.getDriverNames(statisticsForAdminQuery);
            List<StatisticsForAdminVo> driverOut = statisticsForAdminMapper.driverOut(statisticsForAdmin);
            PageInfo<StatisticsForAdminVo> info = new PageInfo<>(driverOut);
            jsonObject.put("driverOut",new PageResult<>(info.getTotal(), info.getPages(), driverOut));
            jsonObject.put("driverNames",driverNames);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }

    @Override
    public ApiResponse appointmentTime(StatisticsForAdmin statisticsForAdmin) {
        List<StatisticsForAdminVo> appointmentTime = statisticsForAdminMapper.appointmentTime(statisticsForAdmin);

        return null;
    }

    //车型使用统计
    @Override
    public ApiResponse modelUse(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        StatisticsForAdmin statisticsForAdminQuery = new StatisticsForAdmin();
        statisticsForAdminQuery.setCompanyId(companyId);
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
            List<String> carNames = statisticsForAdminMapper.getCarNames(statisticsForAdminQuery);
            List<StatisticsForAdminVo> modelUse = statisticsForAdminMapper.modelUse(statisticsForAdmin);
            PageInfo<StatisticsForAdminVo> info = new PageInfo<>(modelUse);
            jsonObject.put("modelUse",new PageResult<>(info.getTotal(), info.getPages(), modelUse));
            jsonObject.put("carNames",carNames);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }

    //租赁情况统计
    @Override
    public ApiResponse leasing(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        StatisticsForAdmin statisticsForAdminQuery = new StatisticsForAdmin();
        statisticsForAdminQuery.setCompanyId(companyId);
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
            List<String> carGroupNames = statisticsForAdminMapper.getCarGroupNames(statisticsForAdminQuery);
            PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
            List<StatisticsForAdminVo> leasing = statisticsForAdminMapper.leasing(statisticsForAdmin);
            PageInfo<StatisticsForAdminVo> info = new PageInfo<>(leasing);
            jsonObject.put("leasing",new PageResult<>(info.getTotal(), info.getPages(), leasing));
            jsonObject.put("carGroupNames",carGroupNames);
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }

    //详情统计
    @Override
    public ApiResponse details(StatisticsForAdmin statisticsForAdmin) {
        Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getOwnerCompany();
        statisticsForAdmin.setCompanyId(companyId);
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
        statisticsForAdmin.setBeginDate(statisticsForAdmin.getBeginDate().substring(0, 10)+" 00:00:00");
        statisticsForAdmin.setEndDate(statisticsForAdmin.getEndDate().substring(0, 10)+" 23:59:59");
        try {
            List<StatisticsForAdminDetailVo> details = statisticsForAdminMapper.details(statisticsForAdmin);
            PageInfo<StatisticsForAdminDetailVo> info = new PageInfo<>(details);
            jsonObject.put("details",new PageResult<>(info.getTotal(), info.getPages(), details));
            apiResponse.setCode(0);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("success");
        }catch (Exception e){
            apiResponse.setCode(1);
            apiResponse.setData(jsonObject);
            apiResponse.setMsg("error");
        }
        return apiResponse;
    }
}
