package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarInfo selectCarInfoById(Long carId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarInfo> selectCarInfoList(CarInfo carInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarInfo(CarInfo carInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarInfo(CarInfo carInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInfoByIds(Long[] carIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInfoById(Long carId);
    /**
     * 可管理车辆总数
     */
    public int queryCompanyCarCount();
}
