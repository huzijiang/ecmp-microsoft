package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarIllegalInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarIllegalInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param illegalId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarIllegalInfo selectCarIllegalInfoById(Long illegalId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarIllegalInfo> selectCarIllegalInfoList(CarIllegalInfo carIllegalInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarIllegalInfo(CarIllegalInfo carIllegalInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarIllegalInfo(CarIllegalInfo carIllegalInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param illegalIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarIllegalInfoByIds(Long[] illegalIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param illegalId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarIllegalInfoById(Long illegalId);
}
