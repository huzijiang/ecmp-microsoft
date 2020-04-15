package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpCityTrafficNodeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpCityTrafficNodeInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param trafficNodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpCityTrafficNodeInfo selectEcmpCityTrafficNodeInfoById(Long trafficNodeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpCityTrafficNodeInfo> selectEcmpCityTrafficNodeInfoList(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpCityTrafficNodeInfo(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpCityTrafficNodeInfo(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param trafficNodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpCityTrafficNodeInfoByIds(Long[] trafficNodeIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param trafficNodeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpCityTrafficNodeInfoById(Long trafficNodeId);
}
