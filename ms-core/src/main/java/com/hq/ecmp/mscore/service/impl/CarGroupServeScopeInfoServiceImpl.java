package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.CarGroupServeScopeInfo;
import com.hq.ecmp.mscore.mapper.CarGroupServeScopeInfoMapper;
import com.hq.ecmp.mscore.service.ICarGroupServeScopeInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CarGroupServeScopeInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-21 12:11:26
 */
@Service("carGroupServeScopeInfoService")
public class CarGroupServeScopeInfoServiceImpl implements ICarGroupServeScopeInfoService {
    @Resource
    private CarGroupServeScopeInfoMapper carGroupServeScopeInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public CarGroupServeScopeInfo queryById(Long id) {
        return this.carGroupServeScopeInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<CarGroupServeScopeInfo> queryAllByLimit(int offset, int limit) {
        return this.carGroupServeScopeInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param carGroupServeScopeInfo 实例对象
     * @return 实例对象
     */
    @Override
    public CarGroupServeScopeInfo insert(CarGroupServeScopeInfo carGroupServeScopeInfo) {
        this.carGroupServeScopeInfoDao.insert(carGroupServeScopeInfo);
        return carGroupServeScopeInfo;
    }

    /**
     * 修改数据
     *
     * @param carGroupServeScopeInfo 实例对象
     * @return 实例对象
     */
    @Override
    public CarGroupServeScopeInfo update(CarGroupServeScopeInfo carGroupServeScopeInfo) {
        this.carGroupServeScopeInfoDao.update(carGroupServeScopeInfo);
        return this.queryById(carGroupServeScopeInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.carGroupServeScopeInfoDao.deleteById(id) > 0;
    }
}
