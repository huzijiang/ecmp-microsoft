package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarHeartbeatInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarHeartbeatInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carHeartbeatId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarHeartbeatInfo selectCarHeartbeatInfoById(String carHeartbeatId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarHeartbeatInfo> selectCarHeartbeatInfoList(CarHeartbeatInfo carHeartbeatInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarHeartbeatInfo(CarHeartbeatInfo carHeartbeatInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarHeartbeatInfo(CarHeartbeatInfo carHeartbeatInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carHeartbeatIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarHeartbeatInfoByIds(String[] carHeartbeatIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carHeartbeatId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarHeartbeatInfoById(String carHeartbeatId);
}
