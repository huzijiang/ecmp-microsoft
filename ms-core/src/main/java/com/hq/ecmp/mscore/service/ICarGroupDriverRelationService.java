package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarGroupDriverRelationService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupDriverRelation selectCarGroupDriverRelationById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupDriverRelation> selectCarGroupDriverRelationList(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDriverRelationByIds(Long[] driverIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDriverRelationById(Long driverId);
}
