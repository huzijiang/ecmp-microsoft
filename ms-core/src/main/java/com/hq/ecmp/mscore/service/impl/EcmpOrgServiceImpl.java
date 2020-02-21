package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.mapper.EcmpOrgMapper;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpOrgServiceImpl implements IEcmpOrgService {
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;

    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    @Override
    public EcmpOrg selectEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.selectEcmpOrgById(deptId);
    }

    /**
     * 根据公司id查询部门对象列表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgsByCompanyId(Long companyId) {
        return ecmpOrgMapper.selectEcmpOrgsByCompanyId(companyId);
    }

    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg) {
        return ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
    }

    /**
     * 新增部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    @Override
    public int insertEcmpOrg(EcmpOrg ecmpOrg) {
        ecmpOrg.setCreateTime(DateUtils.getNowDate());
        return ecmpOrgMapper.insertEcmpOrg(ecmpOrg);
    }

    /**
     * 修改部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    @Override
    public int updateEcmpOrg(EcmpOrg ecmpOrg) {
        ecmpOrg.setUpdateTime(DateUtils.getNowDate());
        return ecmpOrgMapper.updateEcmpOrg(ecmpOrg);
    }

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgByIds(Long[] deptIds) {
        return ecmpOrgMapper.deleteEcmpOrgByIds(deptIds);
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.deleteEcmpOrgById(deptId);
    }
}
