package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户和角色关联Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpUserRoleMapper
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
     * 删除用户和角色关联
     *
     * @param userId 用户和角色关联ID
     * @return 结果
     */
    public int deleteEcmpUserRoleById(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpUserRoleByIds(Long[] userIds);

    String findUserIds(String roleId);

    int deleteUserRole(EcmpUserRole ecmpUserRole);

    /**
     * 根据角色key查询对应的用户id
     * @param roleKey
     * @return
     */
    String findUsersByRoleKey(@Param("roleKey") String roleKey, @Param("deptId") Long deptId);
}
