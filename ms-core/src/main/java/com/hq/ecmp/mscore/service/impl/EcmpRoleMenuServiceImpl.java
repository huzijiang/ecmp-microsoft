package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpRoleMenu;
import com.hq.ecmp.mscore.mapper.EcmpRoleMenuMapper;
import com.hq.ecmp.mscore.service.IEcmpRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpRoleMenuServiceImpl implements IEcmpRoleMenuService
{
    @Autowired
    private EcmpRoleMenuMapper ecmpRoleMenuMapper;

    /**
     * 查询角色和菜单关联
     *
     * @param roleId 角色和菜单关联ID
     * @return 角色和菜单关联
     */
    @Override
    public EcmpRoleMenu selectEcmpRoleMenuById(Long roleId)
    {
        return ecmpRoleMenuMapper.selectEcmpRoleMenuById(roleId);
    }

    /**
     * 查询角色和菜单关联列表
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 角色和菜单关联
     */
    @Override
    public List<EcmpRoleMenu> selectEcmpRoleMenuList(EcmpRoleMenu ecmpRoleMenu)
    {
        return ecmpRoleMenuMapper.selectEcmpRoleMenuList(ecmpRoleMenu);
    }

    /**
     * 新增角色和菜单关联
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 结果
     */
    @Override
    public int insertEcmpRoleMenu(EcmpRoleMenu ecmpRoleMenu)
    {
        return ecmpRoleMenuMapper.insertEcmpRoleMenu(ecmpRoleMenu);
    }

    /**
     * 修改角色和菜单关联
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 结果
     */
    @Override
    public int updateEcmpRoleMenu(EcmpRoleMenu ecmpRoleMenu)
    {
        return ecmpRoleMenuMapper.updateEcmpRoleMenu(ecmpRoleMenu);
    }

    /**
     * 批量删除角色和菜单关联
     *
     * @param roleIds 需要删除的角色和菜单关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleMenuByIds(Long[] roleIds)
    {
        return ecmpRoleMenuMapper.deleteEcmpRoleMenuByIds(roleIds);
    }

    /**
     * 删除角色和菜单关联信息
     *
     * @param roleId 角色和菜单关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleMenuById(Long roleId)
    {
        return ecmpRoleMenuMapper.deleteEcmpRoleMenuById(roleId);
    }
}
