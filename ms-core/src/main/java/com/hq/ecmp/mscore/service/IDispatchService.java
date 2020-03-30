package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.DispatchInfoDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockDriverDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectDriverDto;
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
     * @param dispatchSelectCarDto
     * @return
     */
    ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchSelectCarDto dispatchSelectCarDto);

    /**
     * 调度-锁定选择的车辆
     * @param dispatchLockCarDto
     * @return
     */
    ApiResponse lockSelectedCar(DispatchLockCarDto dispatchLockCarDto);

    /**
     * 调度-解除锁定选择的车辆
     * @param dispatchLockCarDto
     * @return
     */
    ApiResponse unlockSelectedCar(DispatchLockCarDto dispatchLockCarDto);


    /**
     * 调度-获取可选择的司机
     * @param dispatchSelectDriverDto
     * @return
     */

    ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchSelectDriverDto dispatchSelectDriverDto);

    /**
     *  调度-锁定选择的司机
     * @param dispatchLockDriverDto
     * @return
     */
    ApiResponse lockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto);

    /**
     *  调度-解除锁定选择的司机
     * @param dispatchLockDriverDto
     * @return
     */
    ApiResponse unlockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto);


}
