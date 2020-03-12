package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;

import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表服务接口
 *
 * @author shixin
 * @since 2020-03-12 12:08:07
 */
public interface UserAcceptOrderAccountEmailInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAcceptOrderAccountEmailInfo queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param userId 查询起始位置
     * @return 对象列表
     */
    List<UserAcceptOrderAccountEmailInfo> queryAllByUserId(Long userId);

    /**
     * 新增数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 实例对象
     */
    int insert(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);

    /**
     * 修改数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 实例对象
     */
    int update(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    int deleteById(Integer id);

}