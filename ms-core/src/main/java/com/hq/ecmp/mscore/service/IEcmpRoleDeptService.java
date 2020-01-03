package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpRoleDept;

import java.util.List;

/**
 * 角色和部门关联Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpRoleDeptService
{
    /**
     * 查询角色和部门关联
     *
     * @param roleId 角色和部门关联ID
     * @return 角色和部门关联
     */
    public EcmpRoleDept selectEcmpRoleDeptById(Long roleId);

    /**
     * 查询角色和部门关联列表
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 角色和部门关联集合
     */
    public List<EcmpRoleDept> selectEcmpRoleDeptList(EcmpRoleDept ecmpRoleDept);

    /**
     * 新增角色和部门关联
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 结果
     */
    public int insertEcmpRoleDept(EcmpRoleDept ecmpRoleDept);

    /**
     * 修改角色和部门关联
     *
     * @param ecmpRoleDept 角色和部门关联
     * @return 结果
     */
    public int updateEcmpRoleDept(EcmpRoleDept ecmpRoleDept);

    /**
     * 批量删除角色和部门关联
     *
     * @param roleIds 需要删除的角色和部门关联ID
     * @return 结果
     */
    public int deleteEcmpRoleDeptByIds(Long[] roleIds);

    /**
     * 删除角色和部门关联信息
     *
     * @param roleId 角色和部门关联ID
     * @return 结果
     */
    public int deleteEcmpRoleDeptById(Long roleId);
}
