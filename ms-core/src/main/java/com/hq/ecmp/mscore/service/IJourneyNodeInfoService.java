package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.JourneyNodeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IJourneyNodeInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param nodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyNodeInfo selectJourneyNodeInfoById(Long nodeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyNodeInfo> selectJourneyNodeInfoList(JourneyNodeInfo journeyNodeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyNodeInfo(JourneyNodeInfo journeyNodeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyNodeInfo(JourneyNodeInfo journeyNodeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param nodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyNodeInfoByIds(Long[] nodeIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param nodeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyNodeInfoById(Long nodeId);
    
    /**
     * 获取差旅所有的城市名称
     * @param journeyId
     * @return
     */
    public List<String> selectAllCity(Long journeyId);
    
    
    /**
     * 获取指定行程下的所有行程节点中的最早开始时间   和最晚结束时间
     * @param journeyId
     * @return
     */
    public JourneyNodeInfo selectMaxAndMinDate(Long journeyId);
}
