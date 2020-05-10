package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigCityInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface ICostConfigCityInfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCityInfo selectCostConfigCityInfoById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCityInfo> selectCostConfigCityInfoList(CostConfigCityInfo costConfigCityInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCityInfoByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCityInfoById(String id);
}