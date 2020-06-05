package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyAddressInfo;
import com.hq.ecmp.mscore.mapper.JourneyAddressInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyAddressInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-06-04
 */
@Service
public class JourneyAddressInfoServiceImpl implements IJourneyAddressInfoService
{
    @Autowired
    private JourneyAddressInfoMapper journeyAddressInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param addressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyAddressInfo selectJourneyAddressInfoById(Long addressId)
    {
        return journeyAddressInfoMapper.selectJourneyAddressInfoById(addressId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyAddressInfo> selectJourneyAddressInfoList(JourneyAddressInfo journeyAddressInfo)
    {
        return journeyAddressInfoMapper.selectJourneyAddressInfoList(journeyAddressInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyAddressInfo(JourneyAddressInfo journeyAddressInfo)
    {
        journeyAddressInfo.setCreateTime(DateUtils.getNowDate());
        return journeyAddressInfoMapper.insertJourneyAddressInfo(journeyAddressInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyAddressInfo(JourneyAddressInfo journeyAddressInfo)
    {
        journeyAddressInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyAddressInfoMapper.updateJourneyAddressInfo(journeyAddressInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param addressIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyAddressInfoByIds(Long[] addressIds)
    {
        return journeyAddressInfoMapper.deleteJourneyAddressInfoByIds(addressIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param addressId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyAddressInfoById(Long addressId)
    {
        return journeyAddressInfoMapper.deleteJourneyAddressInfoById(addressId);
    }
}