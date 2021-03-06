package com.hq.ecmp.mscore.mapper;


import com.hq.ecmp.mscore.domain.CarGroupServeOrgRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (CarGroupServeOrgRelation)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-05 13:40:29
 */
@Repository
public interface CarGroupServeOrgRelationMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    List<CarGroupServeOrgRelation> queryById(Long carGroupId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CarGroupServeOrgRelation> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 对象列表
     */
    List<CarGroupServeOrgRelation> queryAll(CarGroupServeOrgRelation carGroupServeOrgRelation);

    /**
     * 新增数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 影响行数
     */
    int insert(CarGroupServeOrgRelation carGroupServeOrgRelation);

    /**
     * 修改数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 影响行数
     */
    int update(CarGroupServeOrgRelation carGroupServeOrgRelation);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 影响行数
     */
    int deleteById(Long carGroupId );

}
