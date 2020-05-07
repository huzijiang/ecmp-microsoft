package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface ICostConfigCarTypeInfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCarTypeInfo selectCostConfigCarTypeInfoById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCarTypeInfo> selectCostConfigCarTypeInfoList(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarTypeInfoByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarTypeInfoById(String id);
}