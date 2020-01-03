package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpUserPost;
import com.hq.ecmp.mscore.mapper.EcmpUserPostMapper;
import com.hq.ecmp.mscore.service.IEcmpUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户与岗位关联Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserPostServiceImpl implements IEcmpUserPostService
{
    @Autowired
    private EcmpUserPostMapper ecmpUserPostMapper;

    /**
     * 查询用户与岗位关联
     *
     * @param userId 用户与岗位关联ID
     * @return 用户与岗位关联
     */
    @Override
    public EcmpUserPost selectEcmpUserPostById(Long userId)
    {
        return ecmpUserPostMapper.selectEcmpUserPostById(userId);
    }

    /**
     * 查询用户与岗位关联列表
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 用户与岗位关联
     */
    @Override
    public List<EcmpUserPost> selectEcmpUserPostList(EcmpUserPost ecmpUserPost)
    {
        return ecmpUserPostMapper.selectEcmpUserPostList(ecmpUserPost);
    }

    /**
     * 新增用户与岗位关联
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 结果
     */
    @Override
    public int insertEcmpUserPost(EcmpUserPost ecmpUserPost)
    {
        return ecmpUserPostMapper.insertEcmpUserPost(ecmpUserPost);
    }

    /**
     * 修改用户与岗位关联
     *
     * @param ecmpUserPost 用户与岗位关联
     * @return 结果
     */
    @Override
    public int updateEcmpUserPost(EcmpUserPost ecmpUserPost)
    {
        return ecmpUserPostMapper.updateEcmpUserPost(ecmpUserPost);
    }

    /**
     * 批量删除用户与岗位关联
     *
     * @param userIds 需要删除的用户与岗位关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserPostByIds(Long[] userIds)
    {
        return ecmpUserPostMapper.deleteEcmpUserPostByIds(userIds);
    }

    /**
     * 删除用户与岗位关联信息
     *
     * @param userId 用户与岗位关联ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserPostById(Long userId)
    {
        return ecmpUserPostMapper.deleteEcmpUserPostById(userId);
    }
}
