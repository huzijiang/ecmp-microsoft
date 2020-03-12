package com.hq.ecmp.mscore.mapper;
import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.sun.jna.platform.win32.WinDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表数据库访问层
 *
 * @author shixin
 * @since 2020-03-12 12:04:31
 */
@Repository
public interface UserAcceptOrderAccountEmailInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAcceptOrderAccountEmailInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param userId 用户Id
     * @return 对象列表
     */
    List<UserAcceptOrderAccountEmailInfo> queryAllByLimit(@Param("用户ID") Long userId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 对象列表
     */
    List<UserAcceptOrderAccountEmailInfo> queryAll(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);

    /**
     * 新增数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 影响行数
     */
    int insert(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);

    /**
     * 修改数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 影响行数
     */
    int update(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}