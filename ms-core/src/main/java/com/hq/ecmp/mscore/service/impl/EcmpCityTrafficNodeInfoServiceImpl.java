package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpCityTrafficNodeInfo;
import com.hq.ecmp.mscore.mapper.EcmpCityTrafficNodeInfoMapper;
import com.hq.ecmp.mscore.service.IEcmpCityTrafficNodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpCityTrafficNodeInfoServiceImpl implements IEcmpCityTrafficNodeInfoService
{
    @Autowired
    private EcmpCityTrafficNodeInfoMapper ecmpCityTrafficNodeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param trafficNodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpCityTrafficNodeInfo selectEcmpCityTrafficNodeInfoById(Long trafficNodeId)
    {
        return ecmpCityTrafficNodeInfoMapper.selectEcmpCityTrafficNodeInfoById(trafficNodeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpCityTrafficNodeInfo> selectEcmpCityTrafficNodeInfoList(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo)
    {
        return ecmpCityTrafficNodeInfoMapper.selectEcmpCityTrafficNodeInfoList(ecmpCityTrafficNodeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpCityTrafficNodeInfo(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo)
    {
        ecmpCityTrafficNodeInfo.setCreateTime(DateUtils.getNowDate());
        return ecmpCityTrafficNodeInfoMapper.insertEcmpCityTrafficNodeInfo(ecmpCityTrafficNodeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpCityTrafficNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpCityTrafficNodeInfo(EcmpCityTrafficNodeInfo ecmpCityTrafficNodeInfo)
    {
        ecmpCityTrafficNodeInfo.setUpdateTime(DateUtils.getNowDate());
        return ecmpCityTrafficNodeInfoMapper.updateEcmpCityTrafficNodeInfo(ecmpCityTrafficNodeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param trafficNodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpCityTrafficNodeInfoByIds(Long[] trafficNodeIds)
    {
        return ecmpCityTrafficNodeInfoMapper.deleteEcmpCityTrafficNodeInfoByIds(trafficNodeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param trafficNodeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpCityTrafficNodeInfoById(Long trafficNodeId)
    {
        return ecmpCityTrafficNodeInfoMapper.deleteEcmpCityTrafficNodeInfoById(trafficNodeId);
    }
}
