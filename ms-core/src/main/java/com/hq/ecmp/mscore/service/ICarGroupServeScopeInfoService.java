package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarGroupServeScopeInfo;

import java.util.List;

/**
 * (CarGroupServeScopeInfo)表服务接口
 *
 * @author makejava
 * @since 2020-03-21 12:11:25
 */
public interface ICarGroupServeScopeInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CarGroupServeScopeInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CarGroupServeScopeInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param carGroupServeScopeInfo 实例对象
     * @return 实例对象
     */
    CarGroupServeScopeInfo insert(CarGroupServeScopeInfo carGroupServeScopeInfo);

    /**
     * 修改数据
     *
     * @param carGroupServeScopeInfo 实例对象
     * @return 实例对象
     */
    CarGroupServeScopeInfo update(CarGroupServeScopeInfo carGroupServeScopeInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
