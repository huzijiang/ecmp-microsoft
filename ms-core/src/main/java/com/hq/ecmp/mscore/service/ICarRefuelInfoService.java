package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarRefuelInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarRefuelInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param refuelId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarRefuelInfo selectCarRefuelInfoById(Long refuelId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarRefuelInfo> selectCarRefuelInfoList(CarRefuelInfo carRefuelInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarRefuelInfo(CarRefuelInfo carRefuelInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarRefuelInfo(CarRefuelInfo carRefuelInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param refuelIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarRefuelInfoByIds(Long[] refuelIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param refuelId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarRefuelInfoById(Long refuelId);
}
