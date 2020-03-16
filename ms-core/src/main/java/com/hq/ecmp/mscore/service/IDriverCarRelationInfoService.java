package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IDriverCarRelationInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverCarRelationInfo selectDriverCarRelationInfoById(Long userId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverCarRelationInfo> selectDriverCarRelationInfoList(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverCarRelationInfoByIds(Long[] userIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverCarRelationInfoById(Long userId);
    
    public Integer batchDriverCarList(DriverCarRelationInfo driverCarRelationInfo);
    
    /**
     * 查询驾驶员可以使用的车辆数
     * @param driverId
     * @return
     */
    public Integer queryDriverUseCarCount(Long driverId);
}
