package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpRole;
import com.hq.ecmp.mscore.mapper.EcmpRoleMapper;
import com.hq.ecmp.mscore.service.IEcmpRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色信息Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpRoleServiceImpl implements IEcmpRoleService
{
    @Autowired
    private EcmpRoleMapper ecmpRoleMapper;

    /**
     * 查询角色信息
     *
     * @param roleId 角色信息ID
     * @return 角色信息
     */
    @Override
    public EcmpRole selectEcmpRoleById(Long roleId)
    {
        return ecmpRoleMapper.selectEcmpRoleById(roleId);
    }

    /**
     * 查询角色信息列表
     *
     * @param ecmpRole 角色信息
     * @return 角色信息
     */
    @Override
    public List<EcmpRole> selectEcmpRoleList(EcmpRole ecmpRole)
    {
        return ecmpRoleMapper.selectEcmpRoleList(ecmpRole);
    }

    /**
     * 新增角色信息
     *
     * @param ecmpRole 角色信息
     * @return 结果
     */
    @Override
    public int insertEcmpRole(EcmpRole ecmpRole)
    {
        ecmpRole.setCreateTime(DateUtils.getNowDate());
        return ecmpRoleMapper.insertEcmpRole(ecmpRole);
    }

    /**
     * 修改角色信息
     *
     * @param ecmpRole 角色信息
     * @return 结果
     */
    @Override
    public int updateEcmpRole(EcmpRole ecmpRole)
    {
        ecmpRole.setUpdateTime(DateUtils.getNowDate());
        return ecmpRoleMapper.updateEcmpRole(ecmpRole);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleByIds(Long[] roleIds)
    {
        return ecmpRoleMapper.deleteEcmpRoleByIds(roleIds);
    }

    /**
     * 删除角色信息信息
     *
     * @param roleId 角色信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleById(Long roleId)
    {
        return ecmpRoleMapper.deleteEcmpRoleById(roleId);
    }
}
