package com.hq.ecmp.mscore.service.impl;


import com.hq.ecmp.mscore.domain.CarGroupServeOrgRelation;
import com.hq.ecmp.mscore.mapper.CarGroupServeOrgRelationMapper;

import com.hq.ecmp.mscore.service.CarGroupServeOrgRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CarGroupServeOrgRelation)表服务实现类
 *
 * @author makejava
 * @since 2020-05-05 13:53:54
 */
@Service("carGroupServeOrgRelationService")
public class CarGroupServeOrgRelationServiceImpl implements CarGroupServeOrgRelationService {
    @Resource
    private CarGroupServeOrgRelationMapper carGroupServeOrgRelationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    @Override
    public List<CarGroupServeOrgRelation> queryById(Long carGroupId ) {
        return this.carGroupServeOrgRelationDao.queryById(carGroupId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<CarGroupServeOrgRelation> queryAllByLimit(int offset, int limit) {
        return this.carGroupServeOrgRelationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 实例对象
     */
    @Override
    public CarGroupServeOrgRelation insert(CarGroupServeOrgRelation carGroupServeOrgRelation) {
        this.carGroupServeOrgRelationDao.insert(carGroupServeOrgRelation);
        return carGroupServeOrgRelation;
    }

    /**
     * 修改数据
     *
     * @param carGroupServeOrgRelation 实例对象
     * @return 实例对象
     */
    @Override
    public CarGroupServeOrgRelation update(CarGroupServeOrgRelation carGroupServeOrgRelation) {
        this.carGroupServeOrgRelationDao.update(carGroupServeOrgRelation);
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long carGroupId ) {
        return this.carGroupServeOrgRelationDao.deleteById(carGroupId) > 0;
    }
}