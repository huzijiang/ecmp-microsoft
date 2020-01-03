package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpRoleMenu;

import java.util.List;

/**
 * 角色和菜单关联Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpRoleMenuMapper
{
    /**
     * 查询角色和菜单关联
     *
     * @param roleId 角色和菜单关联ID
     * @return 角色和菜单关联
     */
    public EcmpRoleMenu selectEcmpRoleMenuById(Long roleId);

    /**
     * 查询角色和菜单关联列表
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 角色和菜单关联集合
     */
    public List<EcmpRoleMenu> selectEcmpRoleMenuList(EcmpRoleMenu ecmpRoleMenu);

    /**
     * 新增角色和菜单关联
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 结果
     */
    public int insertEcmpRoleMenu(EcmpRoleMenu ecmpRoleMenu);

    /**
     * 修改角色和菜单关联
     *
     * @param ecmpRoleMenu 角色和菜单关联
     * @return 结果
     */
    public int updateEcmpRoleMenu(EcmpRoleMenu ecmpRoleMenu);

    /**
     * 删除角色和菜单关联
     *
     * @param roleId 角色和菜单关联ID
     * @return 结果
     */
    public int deleteEcmpRoleMenuById(Long roleId);

    /**
     * 批量删除角色和菜单关联
     *
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpRoleMenuByIds(Long[] roleIds);
}
