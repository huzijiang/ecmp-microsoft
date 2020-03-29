package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.vo.CarCostVO;
import com.hq.ecmp.mscore.vo.EstimatePriceVo;
import com.hq.ecmp.mscore.vo.FlightInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/3/20 18:03
 * @Version 1.0
 */
public interface ThirdService {

    /**
     * 通过航班号和航班日期，查询航班信息
     * @param flightCode
     * @param planDate
     * @return
     */
    FlightInfoVo loadDepartment(String flightCode, String planDate);

    /**
     * 通过经纬度查询长短地址
     * @param longitude
     * @param latitude
     */
    Map<String,String> locationByLongitudeAndLatitude(String longitude, String latitude) throws Exception;

    /**
     * 查询车型对应的预估价格
     * @param estimatePriceVo
     * @return
     */
    List<CarCostVO> enterpriseOrderGetCalculatePrice(EstimatePriceVo estimatePriceVo);
}
