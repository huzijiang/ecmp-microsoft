package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.domain.CarGroupServeOrgRelation;

import java.util.List;

/**
 * (CarGroupServeOrgRelation)表服务接口
 *
 * @author makejava
 * @since 2020-05-05 13:40:29
 */
public interface CarGroupServeOrgRelationService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    List<CarGroupServeOrgRelation> queryById(Long carGroupId );

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CarGroupServeOrgRelation> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 实例对象
     */
    CarGroupServeOrgRelation insert(CarGroupServeOrgRelation carGroupServeOrgRelation);

    /**
     * 修改数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 实例对象
     */
    CarGroupServeOrgRelation update(CarGroupServeOrgRelation carGroupServeOrgRelation);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById(Long carGroupId );

}