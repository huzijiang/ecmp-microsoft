package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpEnterpriseInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param enterpriseId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpEnterpriseInfo selectEcmpEnterpriseInfoById(Long enterpriseId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpEnterpriseInfo> selectEcmpEnterpriseInfoList(EcmpEnterpriseInfo ecmpEnterpriseInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpEnterpriseInfo(EcmpEnterpriseInfo ecmpEnterpriseInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpEnterpriseInfo(EcmpEnterpriseInfo ecmpEnterpriseInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param enterpriseId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpEnterpriseInfoById(Long enterpriseId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param enterpriseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpEnterpriseInfoByIds(Long[] enterpriseIds);
}
