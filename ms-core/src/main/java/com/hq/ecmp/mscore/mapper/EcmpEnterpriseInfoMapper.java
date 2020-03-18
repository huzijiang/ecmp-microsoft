package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpEnterpriseInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param enterpriseId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpEnterpriseInvitationInfo selectEcmpEnterpriseInfoById(Long enterpriseId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpEnterpriseInvitationInfo> selectEcmpEnterpriseInfoList(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpEnterpriseInfo(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpEnterpriseInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpEnterpriseInfo(EcmpEnterpriseInvitationInfo ecmpEnterpriseInfo);

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
