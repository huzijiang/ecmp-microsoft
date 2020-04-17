package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.bo.WeatherAndCity;
import com.hq.ecmp.mscore.dto.DirectionDto;
import com.hq.ecmp.mscore.vo.CarCostVO;
import com.hq.ecmp.mscore.vo.EstimatePriceVo;
import com.hq.ecmp.mscore.vo.FlightInfoVo;

import java.net.SocketTimeoutException;
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
    List<CarCostVO> enterpriseOrderGetCalculatePrice(EstimatePriceVo estimatePriceVo) throws Exception;

    /**
     * 通过出发地和目的地经纬度查询时长和里程
     * @param startPoint (格式：精度,纬度)
     * @param endPoint (格式：精度,纬度)
     * @return
     */
    DirectionDto drivingRoute(String startPoint, String endPoint);
    
    
    /**
     * 通过经纬度查询天气，城市
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    WeatherAndCity queryWeatherAndCity(String longitude,String latitude) throws Exception ;

    /**
     * 获取客服电话
     * @return
     * @throws Exception
     */
    String getCustomerPhone() throws Exception;

}
