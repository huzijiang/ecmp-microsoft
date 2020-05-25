package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigCarGroupInfo;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-05-24
 */
public interface ICostConfigCarGroupInfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCarGroupInfo selectCostConfigCarGroupInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCarGroupInfo> selectCostConfigCarGroupInfoList(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarGroupInfoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarGroupInfoById(Long id);
}
