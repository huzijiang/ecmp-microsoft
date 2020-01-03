package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.mapper.CarGroupDriverRelationMapper;
import com.hq.ecmp.mscore.service.ICarGroupDriverRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarGroupDriverRelationServiceImpl implements ICarGroupDriverRelationService
{
    @Autowired
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarGroupDriverRelation selectCarGroupDriverRelationById(Long driverId)
    {
        return carGroupDriverRelationMapper.selectCarGroupDriverRelationById(driverId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarGroupDriverRelation> selectCarGroupDriverRelationList(CarGroupDriverRelation carGroupDriverRelation)
    {
        return carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation)
    {
        carGroupDriverRelation.setCreateTime(DateUtils.getNowDate());
        return carGroupDriverRelationMapper.insertCarGroupDriverRelation(carGroupDriverRelation);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation)
    {
        carGroupDriverRelation.setUpdateTime(DateUtils.getNowDate());
        return carGroupDriverRelationMapper.updateCarGroupDriverRelation(carGroupDriverRelation);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupDriverRelationByIds(Long[] driverIds)
    {
        return carGroupDriverRelationMapper.deleteCarGroupDriverRelationByIds(driverIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupDriverRelationById(Long driverId)
    {
        return carGroupDriverRelationMapper.deleteCarGroupDriverRelationById(driverId);
    }
}
