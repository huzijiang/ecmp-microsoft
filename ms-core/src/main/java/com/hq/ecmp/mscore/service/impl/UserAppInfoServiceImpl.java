package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.UserAppInfo;
import com.hq.ecmp.mscore.mapper.UserAppInfoMapper;
import com.hq.ecmp.mscore.service.IUserAppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class UserAppInfoServiceImpl implements IUserAppInfoService
{
    @Autowired
    private UserAppInfoMapper userAppInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public UserAppInfo selectUserAppInfoById(Long id)
    {
        return userAppInfoMapper.selectUserAppInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserAppInfo> selectUserAppInfoList(UserAppInfo userAppInfo)
    {
        return userAppInfoMapper.selectUserAppInfoList(userAppInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserAppInfo(UserAppInfo userAppInfo)
    {
        userAppInfo.setCreateTime(DateUtils.getNowDate());
        return userAppInfoMapper.insertUserAppInfo(userAppInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserAppInfo(UserAppInfo userAppInfo)
    {
        userAppInfo.setUpdateTime(DateUtils.getNowDate());
        return userAppInfoMapper.updateUserAppInfo(userAppInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserAppInfoByIds(Long[] ids)
    {
        return userAppInfoMapper.deleteUserAppInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserAppInfoById(Long id)
    {
        return userAppInfoMapper.deleteUserAppInfoById(id);
    }
}
