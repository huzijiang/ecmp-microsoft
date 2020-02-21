package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpUser;

import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserService {


    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户信息ID
     * @return 用户信息
     */
    public EcmpUser selectEcmpUserById(Long userId);

    /**
     * 查询用户信息列表
     *
     * @param ecmpUser 用户信息
     * @return 用户信息集合
     */
    public List<EcmpUser> selectEcmpUserList(EcmpUser ecmpUser);

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int insertEcmpUser(EcmpUser ecmpUser);

    /**
     * 修改用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int updateEcmpUser(EcmpUser ecmpUser);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserByIds(Long[] userIds);

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserById(Long userId);


}
