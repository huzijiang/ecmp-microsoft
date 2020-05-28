package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverNatureInfo;
import org.apache.ibatis.annotations.Param;

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
    DriverNatureInfo getDriverNatureInfo(@Param("driverId") Long driverId)throws Exception;

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
