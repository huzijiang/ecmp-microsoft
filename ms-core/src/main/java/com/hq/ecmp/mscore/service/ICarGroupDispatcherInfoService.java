package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarGroupDispatcherInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupDispatcherInfo selectCarGroupDispatcherInfoById(Long dispatcherId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupDispatcherInfo> selectCarGroupDispatcherInfoList(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param dispatcherIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDispatcherInfoByIds(Long[] dispatcherIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDispatcherInfoById(Long dispatcherId);
}
