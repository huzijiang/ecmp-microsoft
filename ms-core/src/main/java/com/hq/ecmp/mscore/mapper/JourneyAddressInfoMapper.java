package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.JourneyAddressInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-06-04
 */
public interface JourneyAddressInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param addressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyAddressInfo selectJourneyAddressInfoById(Long addressId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyAddressInfo> selectJourneyAddressInfoList(JourneyAddressInfo journeyAddressInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyAddressInfo(JourneyAddressInfo journeyAddressInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param journeyAddressInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyAddressInfo(JourneyAddressInfo journeyAddressInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param addressId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyAddressInfoById(Long addressId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param addressIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJourneyAddressInfoByIds(Long[] addressIds);
}