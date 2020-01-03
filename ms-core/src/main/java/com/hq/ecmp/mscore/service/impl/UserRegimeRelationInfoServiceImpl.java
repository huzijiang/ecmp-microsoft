package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.UserRegimeRelationInfo;
import com.hq.ecmp.mscore.mapper.UserRegimeRelationInfoMapper;
import com.hq.ecmp.mscore.service.IUserRegimeRelationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class UserRegimeRelationInfoServiceImpl implements IUserRegimeRelationInfoService
{
    @Autowired
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public UserRegimeRelationInfo selectUserRegimeRelationInfoById(Long userId)
    {
        return userRegimeRelationInfoMapper.selectUserRegimeRelationInfoById(userId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserRegimeRelationInfo> selectUserRegimeRelationInfoList(UserRegimeRelationInfo userRegimeRelationInfo)
    {
        return userRegimeRelationInfoMapper.selectUserRegimeRelationInfoList(userRegimeRelationInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserRegimeRelationInfo(UserRegimeRelationInfo userRegimeRelationInfo)
    {
        return userRegimeRelationInfoMapper.insertUserRegimeRelationInfo(userRegimeRelationInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserRegimeRelationInfo(UserRegimeRelationInfo userRegimeRelationInfo)
    {
        return userRegimeRelationInfoMapper.updateUserRegimeRelationInfo(userRegimeRelationInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserRegimeRelationInfoByIds(Long[] userIds)
    {
        return userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoByIds(userIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserRegimeRelationInfoById(Long userId)
    {
        return userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoById(userId);
    }
}
