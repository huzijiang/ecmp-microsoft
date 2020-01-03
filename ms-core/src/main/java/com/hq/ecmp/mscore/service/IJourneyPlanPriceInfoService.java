package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.JourneyPlanPriceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IJourneyPlanPriceInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param priceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyPlanPriceInfo selectJourneyPlanPriceInfoById(Long priceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyPlanPriceInfo> selectJourneyPlanPriceInfoList(JourneyPlanPriceInfo journeyPlanPriceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyPlanPriceInfo(JourneyPlanPriceInfo journeyPlanPriceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyPlanPriceInfo(JourneyPlanPriceInfo journeyPlanPriceInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param priceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyPlanPriceInfoByIds(Long[] priceIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param priceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyPlanPriceInfoById(Long priceId);
}
