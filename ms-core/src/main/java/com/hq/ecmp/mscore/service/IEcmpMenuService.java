package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpMenu;

import java.util.List;

/**
 * 菜单权限Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpMenuService
{
    /**
     * 查询菜单权限
     *
     * @param menuId 菜单权限ID
     * @return 菜单权限
     */
    public EcmpMenu selectEcmpMenuById(Long menuId);

    /**
     * 查询菜单权限列表
     *
     * @param ecmpMenu 菜单权限
     * @return 菜单权限集合
     */
    public List<EcmpMenu> selectEcmpMenuList(EcmpMenu ecmpMenu);

    /**
     * 新增菜单权限
     *
     * @param ecmpMenu 菜单权限
     * @return 结果
     */
    public int insertEcmpMenu(EcmpMenu ecmpMenu);

    /**
     * 修改菜单权限
     *
     * @param ecmpMenu 菜单权限
     * @return 结果
     */
    public int updateEcmpMenu(EcmpMenu ecmpMenu);

    /**
     * 批量删除菜单权限
     *
     * @param menuIds 需要删除的菜单权限ID
     * @return 结果
     */
    public int deleteEcmpMenuByIds(Long[] menuIds);

    /**
     * 删除菜单权限信息
     *
     * @param menuId 菜单权限ID
     * @return 结果
     */
    public int deleteEcmpMenuById(Long menuId);
}
