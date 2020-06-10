package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsForAdmin;
import com.hq.ecmp.mscore.mapper.StatisticsForAdminMapper;
import com.hq.ecmp.mscore.service.StatisticsForAdminService;
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
    private StatisticsForAdminMapper statisticsForAdminMapper;

    @Override
    public ApiResponse ranking(StatisticsForAdmin statisticsForAdmin) {
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());
        try {
            //出车次数排行
            List<StatisticsForAdminVo> driverOutranking = statisticsForAdminMapper.driverOutranking(statisticsForAdmin);
            Long orders = 0L;
            for (StatisticsForAdminVo statisticsForAdminVo : driverOutranking){
                orders = statisticsForAdminVo.getOrders();
                orders++;
            }
            //用车费用排行
            List<StatisticsForAdminVo> vehicleExpenses = statisticsForAdminMapper.vehicleExpenses(statisticsForAdmin);
            BigDecimal sumAmount = new BigDecimal(0);
            for (StatisticsForAdminVo statisticsForAdminVo : vehicleExpenses){
                sumAmount = sumAmount.add(statisticsForAdminVo.getAmount());
            }
            jsonObject.put("driverOutranking",driverOutranking);
            jsonObject.put("vehicleExpenses",vehicleExpenses);
            jsonObject.put("sumOrder",orders);
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

    @Override
    public ApiResponse unitVehicle(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<String> carUseDepts = new ArrayList<>();
            List<StatisticsForAdminVo> unitVehicleByIn = statisticsForAdminMapper.unitVehicleByIn(statisticsForAdmin);
            for(StatisticsForAdminVo statisticsForAdminVo : unitVehicleByIn){
                carUseDepts.add(statisticsForAdminVo.getDeptName());
            }
            List<StatisticsForAdminVo> unitVehicleByOut = statisticsForAdminMapper.unitVehicleByOut(statisticsForAdmin);
            jsonObject.put("unitVehicleByIn",unitVehicleByIn);
            jsonObject.put("unitVehicleByOut",unitVehicleByOut);
            jsonObject.put("carUseDepts",carUseDepts);
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
    public ApiResponse useOfMechanismVehicles(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<String> carLicenses = new ArrayList<>();
            List<StatisticsForAdminVo> useOfMechanismVehicles = statisticsForAdminMapper.useOfMechanismVehicles(statisticsForAdmin);
            for(StatisticsForAdminVo statisticsForAdminVo : useOfMechanismVehicles){
                carLicenses.add(statisticsForAdminVo.getCarLicense());
            }
            jsonObject.put("useOfMechanismVehicles",useOfMechanismVehicles);
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

    @Override
    public ApiResponse driverOut(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<String> driverNames = new ArrayList<>();
            List<StatisticsForAdminVo> driverOut = statisticsForAdminMapper.driverOut(statisticsForAdmin);
            for (StatisticsForAdminVo statisticsForAdminVo : driverOut){
                driverNames.add(statisticsForAdminVo.getDriverName());
            }
            jsonObject.put("driverOut",driverOut);
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

    @Override
    public ApiResponse modelUse(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<String> carNames = new ArrayList<>();
            List<StatisticsForAdminVo> modelUse = statisticsForAdminMapper.modelUse(statisticsForAdmin);
            for(StatisticsForAdminVo statisticsForAdminVo : modelUse){
                carNames.add(statisticsForAdminVo.getCarName());
            }
            jsonObject.put("modelUse",modelUse);
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

    @Override
    public ApiResponse leasing(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<StatisticsForAdminVo> leasing = statisticsForAdminMapper.leasing(statisticsForAdmin);
            jsonObject.put("leasing",leasing);
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
    public ApiResponse details(StatisticsForAdmin statisticsForAdmin) {

        ApiResponse apiResponse = new ApiResponse();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(statisticsForAdmin.getPageNum(),statisticsForAdmin.getPageSize());

        try {
            List<StatisticsForAdminVo> details = statisticsForAdminMapper.details(statisticsForAdmin);
            jsonObject.put("details",details);
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
