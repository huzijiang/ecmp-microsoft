package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarInsuranceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarInsuranceInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carInsuranceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarInsuranceInfo selectCarInsuranceInfoById(Long carInsuranceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarInsuranceInfo> selectCarInsuranceInfoList(CarInsuranceInfo carInsuranceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarInsuranceInfo(CarInsuranceInfo carInsuranceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarInsuranceInfo(CarInsuranceInfo carInsuranceInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carInsuranceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInsuranceInfoByIds(Long[] carInsuranceIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carInsuranceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInsuranceInfoById(Long carInsuranceId);
}
