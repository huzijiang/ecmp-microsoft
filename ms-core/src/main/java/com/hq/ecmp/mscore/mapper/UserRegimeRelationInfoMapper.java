package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserRegimeRelationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface UserRegimeRelationInfoMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UserRegimeRelationInfo selectUserRegimeRelationInfoById(Long userId);

    /**
     * 根据用户id查询制度id集合
     *
     * @param userId
     * @return
     */
    List<Long> selectIdsByUserId(Long userId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserRegimeRelationInfo> selectUserRegimeRelationInfoList(UserRegimeRelationInfo userRegimeRelationInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertUserRegimeRelationInfo(UserRegimeRelationInfo userRegimeRelationInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param userRegimeRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateUserRegimeRelationInfo(UserRegimeRelationInfo userRegimeRelationInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserRegimeRelationInfoById(Long userId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserRegimeRelationInfoByIds(Long[] userIds);


}

