package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hq.ecmp.mscore.mapper.CostConfigCarGroupInfoMapper;
import com.hq.ecmp.mscore.domain.CostConfigCarGroupInfo;
import com.hq.ecmp.mscore.service.ICostConfigCarGroupInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-24
 */
@Service
public class CostConfigCarGroupInfoServiceImpl implements ICostConfigCarGroupInfoService 
{
    @Autowired
    private CostConfigCarGroupInfoMapper costConfigCarGroupInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CostConfigCarGroupInfo selectCostConfigCarGroupInfoById(Long id)
    {
        return costConfigCarGroupInfoMapper.selectCostConfigCarGroupInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CostConfigCarGroupInfo> selectCostConfigCarGroupInfoList(CostConfigCarGroupInfo costConfigCarGroupInfo)
    {
        return costConfigCarGroupInfoMapper.selectCostConfigCarGroupInfoList(costConfigCarGroupInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo)
    {
        costConfigCarGroupInfo.setCreateTime(DateUtils.getNowDate());
        return costConfigCarGroupInfoMapper.insertCostConfigCarGroupInfo(costConfigCarGroupInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo)
    {
        costConfigCarGroupInfo.setUpdateTime(DateUtils.getNowDate());
        return costConfigCarGroupInfoMapper.updateCostConfigCarGroupInfo(costConfigCarGroupInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCarGroupInfoByIds(Long[] ids)
    {
        return costConfigCarGroupInfoMapper.deleteCostConfigCarGroupInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigCarGroupInfoById(Long id)
    {
        return costConfigCarGroupInfoMapper.deleteCostConfigCarGroupInfoById(id);
    }
}
