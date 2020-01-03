package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserCallPoliceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface UserCallPoliceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UserCallPoliceInfo selectUserCallPoliceInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserCallPoliceInfo> selectUserCallPoliceInfoList(UserCallPoliceInfo userCallPoliceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertUserCallPoliceInfo(UserCallPoliceInfo userCallPoliceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param userCallPoliceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateUserCallPoliceInfo(UserCallPoliceInfo userCallPoliceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserCallPoliceInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserCallPoliceInfoByIds(Long[] ids);
}
