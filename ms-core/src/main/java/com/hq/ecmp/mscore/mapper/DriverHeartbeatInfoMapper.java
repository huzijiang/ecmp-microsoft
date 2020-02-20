package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface DriverHeartbeatInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param heartId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverHeartbeatInfo selectDriverHeartbeatInfoById(Long heartId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverHeartbeatInfo> selectDriverHeartbeatInfoList(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param heartId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverHeartbeatInfoById(Long heartId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param heartIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverHeartbeatInfoByIds(Long[] heartIds);
}
