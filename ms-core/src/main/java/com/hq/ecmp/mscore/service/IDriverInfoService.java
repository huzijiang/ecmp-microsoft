package com.hq.ecmp.mscore.service;

import java.util.List;
import java.util.Date;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.dto.DriverCanUseCarsDTO;
import com.hq.ecmp.mscore.dto.DriverCarDTO;
import com.hq.ecmp.mscore.dto.DriverLoseDTO;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IDriverInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverInfo selectDriverInfoById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverInfo> selectDriverInfoList(DriverInfo driverInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverInfo(DriverInfo driverInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverInfo(DriverInfo driverInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverInfoByIds(Long[] driverIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */

    public int deleteDriverInfoById(Long driverId);

    public boolean createDriver(DriverCreateInfo driverCreateInfo);

    public List<DriverQueryResult> queryDriverList(DriverQuery query);

    public Integer queryDriverListCount(DriverQuery query);

    public DriverQueryResult  queryDriverDetail(Long driverId);
    /**
     *驾驶员总数
     */
    public int queryCompanyDriverCount();
    /**
     *
     * @param phoneNumber
     * @return
     */
    public int driverItisExist(String phoneNumber);

    public int updateDriverStatus(Long driverId,String state);
    /**
     *驾驶员可用车辆列表
     * @param
     */
    public List<DriverCanUseCarsDTO> getDriverCanCar(Long driverId);
    /**
     *驾驶员失效列表,离职列表
     * @param
     */
    public List<DriverLoseDTO> getDriverLoseList(Long deptId);
    /**
     * 驾驶员已离职数量
     */
    public int getDriverLoseCount(Long deptId);
    /**
     * 已失效驾驶员进行删除
     */
    public int deleteDriver(Long driverId);
    /**
     * 修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    public int updateDriver(DriverCreateInfo driverCreateInfo);

    /**
     * 修改驾驶员手机号
     * @param mobile
     * @return
     */
    public int updateDriverMobile(String mobile,Long driverId);

    /**
     * 设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    public int updateDriverDimTime(Date dimTime,Long driverId);
    /**
     * 驾驶员绑定车辆
     * @param driverCarDTO
     * @return
     */
    public void bindDriverCars(DriverCarDTO driverCarDTO, Long userId) throws Exception;
    
    /**
     * 查询指定车队下的可用驾驶员
     * @param carGroupId
     * @return
     */
    public  CarGroupDriverInfo queryCarGroupDriverList(Long carGroupId);


}
