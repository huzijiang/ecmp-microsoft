package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarMaintenanceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface CarMaintenanceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param maintenanceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarMaintenanceInfo selectCarMaintenanceInfoById(Long maintenanceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarMaintenanceInfo> selectCarMaintenanceInfoList(CarMaintenanceInfo carMaintenanceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarMaintenanceInfo(CarMaintenanceInfo carMaintenanceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarMaintenanceInfo(CarMaintenanceInfo carMaintenanceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param maintenanceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarMaintenanceInfoById(Long maintenanceId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param maintenanceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarMaintenanceInfoByIds(Long[] maintenanceIds);
}
