package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserRegimeRelationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IUserRegimeRelationInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UserRegimeRelationInfo selectUserRegimeRelationInfoById(Long userId);

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
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserRegimeRelationInfoByIds(Long[] userIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteUserRegimeRelationInfoById(Long userId);
    
    /**
     * 
     * @param regimenId  制度编号
     * @param userIdList  可用这个制度的用户编号列表
     * @return
     */
    public Integer batchInsertUser(Long regimenId,List<Long> userIdList);
}
