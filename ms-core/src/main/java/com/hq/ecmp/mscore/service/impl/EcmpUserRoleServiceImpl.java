package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpUserRole;
import com.hq.ecmp.mscore.mapper.EcmpUserRoleMapper;
import com.hq.ecmp.mscore.service.IEcmpUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserRoleServiceImpl implements IEcmpUserRoleService
{
    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;

    /**
     * 查询用户和角色关联
     *
     * @param userId 用户和角色关联ID
     * @return 用户和角色关联
     */
    @Override
    public EcmpUserRole selectEcmpUserRoleById(Long userId)
    {
        return ecmpUserRoleMapper.selectEcmpUserRoleById(userId);
    }

    /**
     * 查询用户和角色关联列表
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 用户和角色关联
     */
    @Override
    public List<EcmpUserRole> selectEcmpUserRoleList(EcmpUserRole ecmpUserRole)
    {
        return ecmpUserRoleMapper.selectEcmpUserRoleList(ecmpUserRole);
    }

    /**
     * 新增用户和角色关联
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 结果
     */
    @Override
    public int insertEcmpUserRole(EcmpUserRole ecmpUserRole)
    {
        return ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);
    }

    /**
     * 修改用户和角色关联
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 结果
     */
    @Override
    public int updateEcmpUserRole(EcmpUserRole ecmpUserRole)
    {
        return ecmpUserRoleMapper.updateEcmpUserRole(ecmpUserRole);
    }

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的用户和角色关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserRoleByIds(Long[] userIds)
    {
        return ecmpUserRoleMapper.deleteEcmpUserRoleByIds(userIds);
    }

    /**
     * 删除用户和角色关联信息
     *
     * @param userId 用户和角色关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserRoleById(Long userId)
    {
        return ecmpUserRoleMapper.deleteEcmpUserRoleById(userId);
    }
}
