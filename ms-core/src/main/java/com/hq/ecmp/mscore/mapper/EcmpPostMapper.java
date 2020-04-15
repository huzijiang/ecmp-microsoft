package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpPost;

import java.util.List;

/**
 * 岗位信息Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpPostMapper
{
    /**
     * 查询岗位信息
     *
     * @param postId 岗位信息ID
     * @return 岗位信息
     */
    public EcmpPost selectEcmpPostById(Long postId);

    /**
     * 查询岗位信息列表
     *
     * @param ecmpPost 岗位信息
     * @return 岗位信息集合
     */
    public List<EcmpPost> selectEcmpPostList(EcmpPost ecmpPost);

    /**
     * 新增岗位信息
     *
     * @param ecmpPost 岗位信息
     * @return 结果
     */
    public int insertEcmpPost(EcmpPost ecmpPost);

    /**
     * 修改岗位信息
     *
     * @param ecmpPost 岗位信息
     * @return 结果
     */
    public int updateEcmpPost(EcmpPost ecmpPost);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位信息ID
     * @return 结果
     */
    public int deleteEcmpPostById(Long postId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpPostByIds(Long[] postIds);
}
