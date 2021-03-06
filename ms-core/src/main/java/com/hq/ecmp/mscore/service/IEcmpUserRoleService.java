package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpUserRole;

import java.util.List;

/**
 * 用户和角色关联Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserRoleService
{
    /**
     * 查询用户和角色关联
     *
     * @param userId 用户和角色关联ID
     * @return 用户和角色关联
     */
    public EcmpUserRole selectEcmpUserRoleById(Long userId);

    /**
     * 查询用户和角色关联列表
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 用户和角色关联集合
     */
    public List<EcmpUserRole> selectEcmpUserRoleList(EcmpUserRole ecmpUserRole);

    /**
     * 新增用户和角色关联
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 结果
     */
    public int insertEcmpUserRole(EcmpUserRole ecmpUserRole);

    /**
     * 修改用户和角色关联
     *
     * @param ecmpUserRole 用户和角色关联
     * @return 结果
     */
    public int updateEcmpUserRole(EcmpUserRole ecmpUserRole);

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的用户和角色关联ID
     * @return 结果
     */
    public int deleteEcmpUserRoleByIds(Long[] userIds);

    /**
     * 删除用户和角色关联信息
     *
     * @param userId 用户和角色关联ID
     * @return 结果
     */
    public int deleteEcmpUserRoleById(Long userId);
}
