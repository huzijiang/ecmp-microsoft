package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpRole;

import java.util.List;

/**
 * 角色信息Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpRoleMapper
{
    /**
     * 查询角色信息
     *
     * @param roleId 角色信息ID
     * @return 角色信息
     */
    public EcmpRole selectEcmpRoleById(Long roleId);

    /**
     * 查询角色信息列表
     *
     * @param ecmpRole 角色信息
     * @return 角色信息集合
     */
    public List<EcmpRole> selectEcmpRoleList(EcmpRole ecmpRole);

    /**
     * 新增角色信息
     *
     * @param ecmpRole 角色信息
     * @return 结果
     */
    public int insertEcmpRole(EcmpRole ecmpRole);

    /**
     * 修改角色信息
     *
     * @param ecmpRole 角色信息
     * @return 结果
     */
    public int updateEcmpRole(EcmpRole ecmpRole);

    /**
     * 删除角色信息
     *
     * @param roleId 角色信息ID
     * @return 结果
     */
    public int deleteEcmpRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpRoleByIds(Long[] roleIds);
}
