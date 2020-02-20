package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpMenu;
import com.hq.ecmp.mscore.mapper.EcmpMenuMapper;
import com.hq.ecmp.mscore.service.IEcmpMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单权限Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpMenuServiceImpl implements IEcmpMenuService
{
    @Autowired
    private EcmpMenuMapper ecmpMenuMapper;

    /**
     * 查询菜单权限
     *
     * @param menuId 菜单权限ID
     * @return 菜单权限
     */
    @Override
    public EcmpMenu selectEcmpMenuById(Long menuId)
    {
        return ecmpMenuMapper.selectEcmpMenuById(menuId);
    }

    /**
     * 查询菜单权限列表
     *
     * @param ecmpMenu 菜单权限
     * @return 菜单权限
     */
    @Override
    public List<EcmpMenu> selectEcmpMenuList(EcmpMenu ecmpMenu)
    {
        return ecmpMenuMapper.selectEcmpMenuList(ecmpMenu);
    }

    /**
     * 新增菜单权限
     *
     * @param ecmpMenu 菜单权限
     * @return 结果
     */
    @Override
    public int insertEcmpMenu(EcmpMenu ecmpMenu)
    {
        ecmpMenu.setCreateTime(DateUtils.getNowDate());
        return ecmpMenuMapper.insertEcmpMenu(ecmpMenu);
    }

    /**
     * 修改菜单权限
     *
     * @param ecmpMenu 菜单权限
     * @return 结果
     */
    @Override
    public int updateEcmpMenu(EcmpMenu ecmpMenu)
    {
        ecmpMenu.setUpdateTime(DateUtils.getNowDate());
        return ecmpMenuMapper.updateEcmpMenu(ecmpMenu);
    }

    /**
     * 批量删除菜单权限
     *
     * @param menuIds 需要删除的菜单权限ID
     * @return 结果
     */
    @Override
    public int deleteEcmpMenuByIds(Long[] menuIds)
    {
        return ecmpMenuMapper.deleteEcmpMenuByIds(menuIds);
    }

    /**
     * 删除菜单权限信息
     *
     * @param menuId 菜单权限ID
     * @return 结果
     */
    @Override
    public int deleteEcmpMenuById(Long menuId)
    {
        return ecmpMenuMapper.deleteEcmpMenuById(menuId);
    }
}
