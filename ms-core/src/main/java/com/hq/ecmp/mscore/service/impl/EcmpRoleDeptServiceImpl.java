package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpRoleDept;
import com.hq.ecmp.mscore.mapper.EcmpRoleDeptMapper;
import com.hq.ecmp.mscore.service.IEcmpRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色和部门关联Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpRoleDeptServiceImpl implements IEcmpRoleDeptService
{
    @Autowired
    private EcmpRoleDeptMapper ecmpRoleDeptMapper;

    /**
     * 查询角色和部门关联
     *
     * @param roleId 角色和部门关联ID
     * @return 角色和部门关联
     */
    @Override
    public EcmpRoleDept selectEcmpRoleDeptById(Long roleId)
    {
        return ecmpRoleDeptMapper.selectEcmpRoleDeptById(roleId);
    }

    /**
     * 查询角色和部门关联列表
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 角色和部门关联
     */
    @Override
    public List<EcmpRoleDept> selectEcmpRoleDeptList(EcmpRoleDept ecmpRoleDept)
    {
        return ecmpRoleDeptMapper.selectEcmpRoleDeptList(ecmpRoleDept);
    }

    /**
     * 新增角色和部门关联
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 结果
     */
    @Override
    public int insertEcmpRoleDept(EcmpRoleDept ecmpRoleDept)
    {
        return ecmpRoleDeptMapper.insertEcmpRoleDept(ecmpRoleDept);
    }

    /**
     * 修改角色和部门关联
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 结果
     */
    @Override
    public int updateEcmpRoleDept(EcmpRoleDept ecmpRoleDept)
    {
        return ecmpRoleDeptMapper.updateEcmpRoleDept(ecmpRoleDept);
    }

    /**
     * 批量删除角色和部门关联
     *
     * @param roleIds 需要删除的角色和部门关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleDeptByIds(Long[] roleIds)
    {
        return ecmpRoleDeptMapper.deleteEcmpRoleDeptByIds(roleIds);
    }

    /**
     * 删除角色和部门关联信息
     *
     * @param roleId 角色和部门关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpRoleDeptById(Long roleId)
    {
        return ecmpRoleDeptMapper.deleteEcmpRoleDeptById(roleId);
    }
}
