package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserEmergencyContactInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户紧急联系人信息表(UserEmergencyContactInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-09 23:58:22
 */
public interface UserEmergencyContactInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserEmergencyContactInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserEmergencyContactInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 对象列表
     */
    List<UserEmergencyContactInfo> queryAll(UserEmergencyContactInfo userEmergencyContactInfo);

    /**
     * 新增数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 影响行数
     */
    int insert(UserEmergencyContactInfo userEmergencyContactInfo);

    /**
     * 修改数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 影响行数
     */
    int update(UserEmergencyContactInfo userEmergencyContactInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 通过userId删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    List<UserEmergencyContactInfo> queryByUserId(Long userId);

}