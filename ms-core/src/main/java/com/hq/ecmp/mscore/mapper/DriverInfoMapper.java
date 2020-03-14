package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface DriverInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverInfoById(Long driverId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverInfoByIds(Long[] driverIds);
    
    public Integer createDriver(DriverCreateInfo driverCreateInfo);
    
    public List<DriverQueryResult> queryDriverList(DriverQuery query);
    
    public Integer queryDriverListCount(DriverQuery query);
    
    public DriverQueryResult queryDriverDetail(Long driverId);
}
