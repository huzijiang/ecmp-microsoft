package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpEnterpriseInfoServiceImpl implements IEcmpEnterpriseInfoService
{
    @Autowired
    private EcmpEnterpriseInfoMapper ecmpEnterpriseInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param enterpriseId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpEnterpriseInvitationInfo selectEcmpEnterpriseInfoById(Long enterpriseId)
    {
        return ecmpEnterpriseInfoMapper.selectEcmpEnterpriseInfoById(enterpriseId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpEnterpriseInvitationInfo> selectEcmpEnterpriseInfoList(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo)
    {
        return ecmpEnterpriseInfoMapper.selectEcmpEnterpriseInfoList(ecmpEnterpriseInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpEnterpriseInfo(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo)
    {
        return ecmpEnterpriseInfoMapper.insertEcmpEnterpriseInfo(ecmpEnterpriseInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpEnterpriseInfo(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo)
    {
        return ecmpEnterpriseInfoMapper.updateEcmpEnterpriseInfo(ecmpEnterpriseInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param enterpriseIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpEnterpriseInfoByIds(Long[] enterpriseIds)
    {
        return ecmpEnterpriseInfoMapper.deleteEcmpEnterpriseInfoByIds(enterpriseIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param enterpriseId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpEnterpriseInfoById(Long enterpriseId)
    {
        return ecmpEnterpriseInfoMapper.deleteEcmpEnterpriseInfoById(enterpriseId);
    }
}
