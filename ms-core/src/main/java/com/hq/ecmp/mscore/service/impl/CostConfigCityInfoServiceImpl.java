package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import com.hq.ecmp.mscore.mapper.CostConfigCityInfoMapper;
import com.hq.ecmp.mscore.service.ICostConfigCityInfoService;
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
public class CostConfigCityInfoServiceImpl implements ICostConfigCityInfoService
{
    @Autowired
    private CostConfigCityInfoMapper costConfigCityInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CostConfigCityInfo selectCostConfigCityInfoById(String id)
    {
        return costConfigCityInfoMapper.selectCostConfigCityInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CostConfigCityInfo> selectCostConfigCityInfoList(CostConfigCityInfo costConfigCityInfo)
    {
        return costConfigCityInfoMapper.selectCostConfigCityInfoList(costConfigCityInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo)
    {
        costConfigCityInfo.setCreateTime(DateUtils.getNowDate());
        return costConfigCityInfoMapper.insertCostConfigCityInfo(costConfigCityInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo)
    {
        costConfigCityInfo.setUpdateTime(DateUtils.getNowDate());
        return costConfigCityInfoMapper.updateCostConfigCityInfo(costConfigCityInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCityInfoByIds(String[] ids)
    {
        return costConfigCityInfoMapper.deleteCostConfigCityInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCityInfoById(String id)
    {
        return costConfigCityInfoMapper.deleteCostConfigCityInfoById(id);
    }
}