package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.JourneyNodeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface JourneyNodeInfoMapper
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
     * 新增行程节点信息
     *
     * @param journeyNodeInfo 行程节点信息
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
     * 删除【请填写功能名称】
     *
     * @param nodeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyNodeInfoById(Long nodeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param nodeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJourneyNodeInfoByIds(Long[] nodeIds);
    
    public JourneyNodeInfo selectMaxAndMinDate(Long journeyId);
    
    
    public List<String> queryGroupCity(Long journeyId);
    
    
    public List<JourneyNodeInfo> queryJourneyNodeInfoOrderByNumber(Long journeyId);
}
