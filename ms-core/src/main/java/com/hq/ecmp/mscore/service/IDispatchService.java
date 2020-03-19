package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.DispatchInfoDto;
import com.hq.ecmp.mscore.vo.CarInfoVO;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.mscore.vo.DriverInfoVO;

import java.util.LinkedList;

/**
 * @Author: zj.hu
 * @Date: 2020-03-17 22:53
 */
public interface IDispatchService {

    /**
     * 调度-获取可选择的车辆
     * @param dispatchInfoDto
     * @return
     */
    ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchInfoDto dispatchInfoDto);

    /**
     * 调度-锁定选择的车辆
     * @param dispatchInfoDto
     * @return
     */
    ApiResponse<DispatchResultVo> lockSelectedCar(DispatchInfoDto dispatchInfoDto);
    /**
     * 调度-获取可选择的司机
     * @param dispatchInfoDto
     * @return
     */

    ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchInfoDto dispatchInfoDto);

    /**
     *  调度-锁定选择的司机
     * @param dispatchInfoDto
     * @return
     */
    ApiResponse<DispatchResultVo> lockSelectedDriver(DispatchInfoDto dispatchInfoDto);


}
