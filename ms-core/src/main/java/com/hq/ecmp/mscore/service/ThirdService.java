package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.vo.FlightInfoVo;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/3/20 18:03
 * @Version 1.0
 */
public interface ThirdService {

    FlightInfoVo loadDepartment(String flightCode, String planDate);
}
