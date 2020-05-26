package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverNatureInfo;

import java.util.List;

public interface DriverNatureInfoMapper {

    /***
     *
     * @param driverNatureInfo
     * @return
     */
    int addDriverNatureInfo(DriverNatureInfo driverNatureInfo)throws Exception;


    /***
     *
     * @param driverId
     * @return
     * @throws Exception
     */
    DriverNatureInfo getDriverNatureInfo(Long driverId)throws Exception;

    /***
     *
     * @return
     * @throws Exception
     */
    List<DriverNatureInfo> getDriverNatureInfoList()throws Exception;

    /***
     *
     * @param driverNatureInfo
     * @return
     * @throws Exception
     */
    int updateDriverNatureInfo(DriverNatureInfo driverNatureInfo)throws Exception;

    DriverNatureInfo selectDriverNatureInfoByIncitationId(Long invitationId);
}
