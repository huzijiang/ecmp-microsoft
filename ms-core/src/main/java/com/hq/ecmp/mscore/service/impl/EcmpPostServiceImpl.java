package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpPost;
import com.hq.ecmp.mscore.mapper.EcmpPostMapper;
import com.hq.ecmp.mscore.service.IEcmpPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 岗位信息Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpPostServiceImpl implements IEcmpPostService
{
    @Autowired
    private EcmpPostMapper ecmpPostMapper;

    /**
     * 查询岗位信息
     *
     * @param postId 岗位信息ID
     * @return 岗位信息
     */
    @Override
    public EcmpPost selectEcmpPostById(Long postId)
    {
        return ecmpPostMapper.selectEcmpPostById(postId);
    }

    /**
     * 查询岗位信息列表
     *
     * @param ecmpPost 岗位信息
     * @return 岗位信息
     */
    @Override
    public List<EcmpPost> selectEcmpPostList(EcmpPost ecmpPost)
    {
        return ecmpPostMapper.selectEcmpPostList(ecmpPost);
    }

    /**
     * 新增岗位信息
     *
     * @param ecmpPost 岗位信息
     * @return 结果
     */
    @Override
    public int insertEcmpPost(EcmpPost ecmpPost)
    {
        ecmpPost.setCreateTime(DateUtils.getNowDate());
        return ecmpPostMapper.insertEcmpPost(ecmpPost);
    }

    /**
     * 修改岗位信息
     *
     * @param ecmpPost 岗位信息
     * @return 结果
     */
    @Override
    public int updateEcmpPost(EcmpPost ecmpPost)
    {
        ecmpPost.setUpdateTime(DateUtils.getNowDate());
        return ecmpPostMapper.updateEcmpPost(ecmpPost);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpPostByIds(Long[] postIds)
    {
        return ecmpPostMapper.deleteEcmpPostByIds(postIds);
    }

    /**
     * 删除岗位信息信息
     *
     * @param postId 岗位信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpPostById(Long postId)
    {
        return ecmpPostMapper.deleteEcmpPostById(postId);
    }
}
