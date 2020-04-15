package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.UserCallPoliceInfo;
import com.hq.ecmp.mscore.mapper.UserCallPoliceInfoMapper;
import com.hq.ecmp.mscore.service.IUserCallPoliceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class UserCallPoliceInfoServiceImpl implements IUserCallPoliceInfoService
{
    @Autowired
    private UserCallPoliceInfoMapper userCallPoliceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public UserCallPoliceInfo selectUserCallPoliceInfoById(Long id)
    {
        return userCallPoliceInfoMapper.selectUserCallPoliceInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserCallPoliceInfo> selectUserCallPoliceInfoList(UserCallPoliceInfo userCallPoliceInfo)
    {
        return userCallPoliceInfoMapper.selectUserCallPoliceInfoList(userCallPoliceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserCallPoliceInfo(UserCallPoliceInfo userCallPoliceInfo)
    {
        userCallPoliceInfo.setCreateTime(DateUtils.getNowDate());
        return userCallPoliceInfoMapper.insertUserCallPoliceInfo(userCallPoliceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserCallPoliceInfo(UserCallPoliceInfo userCallPoliceInfo)
    {
        userCallPoliceInfo.setUpdateTime(DateUtils.getNowDate());
        return userCallPoliceInfoMapper.updateUserCallPoliceInfo(userCallPoliceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserCallPoliceInfoByIds(Long[] ids)
    {
        return userCallPoliceInfoMapper.deleteUserCallPoliceInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteUserCallPoliceInfoById(Long id)
    {
        return userCallPoliceInfoMapper.deleteUserCallPoliceInfoById(id);
    }
}
