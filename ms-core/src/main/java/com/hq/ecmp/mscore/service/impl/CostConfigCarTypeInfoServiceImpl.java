package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.mapper.CostConfigCarTypeInfoMapper;
import com.hq.ecmp.mscore.service.ICostConfigCarTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-06
 */
@Service
public class CostConfigCarTypeInfoServiceImpl implements ICostConfigCarTypeInfoService
{
    @Autowired
    private CostConfigCarTypeInfoMapper costConfigCarTypeInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CostConfigCarTypeInfo selectCostConfigCarTypeInfoById(String id)
    {
        return costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CostConfigCarTypeInfo> selectCostConfigCarTypeInfoList(CostConfigCarTypeInfo costConfigCarTypeInfo)
    {
        return costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoList(costConfigCarTypeInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo)
    {
        costConfigCarTypeInfo.setCreateTime(DateUtils.getNowDate());
        return costConfigCarTypeInfoMapper.insertCostConfigCarTypeInfo(costConfigCarTypeInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo)
    {
        costConfigCarTypeInfo.setUpdateTime(DateUtils.getNowDate());
        return costConfigCarTypeInfoMapper.updateCostConfigCarTypeInfo(costConfigCarTypeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCarTypeInfoByIds(String[] ids)
    {
        return costConfigCarTypeInfoMapper.deleteCostConfigCarTypeInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCarTypeInfoById(String id)
    {
        return costConfigCarTypeInfoMapper.deleteCostConfigCarTypeInfoById(id);
    }
}