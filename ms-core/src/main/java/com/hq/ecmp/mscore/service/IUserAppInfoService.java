package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserAppInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IUserAppInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UserAppInfo selectUserAppInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserAppInfo> selectUserAppInfoList(UserAppInfo userAppInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertUserAppInfo(UserAppInfo userAppInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param userAppInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateUserAppInfo(UserAppInfo userAppInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserAppInfoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserAppInfoById(Long id);
}
