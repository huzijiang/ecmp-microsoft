package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarYearlyCheckInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface CarYearlyCheckInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param yearlyCheckId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarYearlyCheckInfo selectCarYearlyCheckInfoById(Long yearlyCheckId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarYearlyCheckInfo> selectCarYearlyCheckInfoList(CarYearlyCheckInfo carYearlyCheckInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarYearlyCheckInfo(CarYearlyCheckInfo carYearlyCheckInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarYearlyCheckInfo(CarYearlyCheckInfo carYearlyCheckInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param yearlyCheckId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarYearlyCheckInfoById(Long yearlyCheckId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param yearlyCheckIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarYearlyCheckInfoByIds(Long[] yearlyCheckIds);
}
