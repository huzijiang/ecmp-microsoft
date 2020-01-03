package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUserPost;

import java.util.List;

/**
 * 用户与岗位关联Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpUserPostMapper
{
    /**
     * 查询用户与岗位关联
     *
     * @param userId 用户与岗位关联ID
     * @return 用户与岗位关联
     */
    public EcmpUserPost selectEcmpUserPostById(Long userId);

    /**
     * 查询用户与岗位关联列表
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 用户与岗位关联集合
     */
    public List<EcmpUserPost> selectEcmpUserPostList(EcmpUserPost ecmpUserPost);

    /**
     * 新增用户与岗位关联
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 结果
     */
    public int insertEcmpUserPost(EcmpUserPost ecmpUserPost);

    /**
     * 修改用户与岗位关联
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 结果
     */
    public int updateEcmpUserPost(EcmpUserPost ecmpUserPost);

    /**
     * 删除用户与岗位关联
     *
     * @param userId 用户与岗位关联ID
     * @return 结果
     */
    public int deleteEcmpUserPostById(Long userId);

    /**
     * 批量删除用户与岗位关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpUserPostByIds(Long[] userIds);
}
