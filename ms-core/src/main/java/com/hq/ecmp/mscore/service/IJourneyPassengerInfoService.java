package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IJourneyPassengerInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyPassengerInfo selectJourneyPassengerInfoById(Long journeyPassengerId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyPassengerInfo> selectJourneyPassengerInfoList(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyPassengerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyPassengerInfoByIds(Long[] journeyPassengerIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyPassengerInfoById(Long journeyPassengerId);

    String getPeerPeople(Long journeyId);
    
    /**
     * 查询指定行程中同行人就多少
     * @param journeyId
     * @return
     */
    public Integer queryPeerCount(Long journeyId);
    
    //查询指定行程中所有同行人
    public List<String> queryPeerUserNameList(Long journeyId);
}
