package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyPlanPriceInfo;
import com.hq.ecmp.mscore.mapper.JourneyPlanPriceInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyPlanPriceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyPlanPriceInfoServiceImpl implements IJourneyPlanPriceInfoService
{
    @Autowired
    private JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param priceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyPlanPriceInfo selectJourneyPlanPriceInfoById(Long priceId)
    {
        return journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoById(priceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyPlanPriceInfo> selectJourneyPlanPriceInfoList(JourneyPlanPriceInfo journeyPlanPriceInfo)
    {
        return journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyPlanPriceInfo(JourneyPlanPriceInfo journeyPlanPriceInfo)
    {
        journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
        return journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPlanPriceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyPlanPriceInfo(JourneyPlanPriceInfo journeyPlanPriceInfo)
    {
        return journeyPlanPriceInfoMapper.updateJourneyPlanPriceInfo(journeyPlanPriceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param priceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPlanPriceInfoByIds(Long[] priceIds)
    {
        return journeyPlanPriceInfoMapper.deleteJourneyPlanPriceInfoByIds(priceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param priceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPlanPriceInfoById(Long priceId)
    {
        return journeyPlanPriceInfoMapper.deleteJourneyPlanPriceInfoById(priceId);
    }
}
