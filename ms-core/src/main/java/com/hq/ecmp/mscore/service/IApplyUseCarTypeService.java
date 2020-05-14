package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ApplyUseCarType;

import java.util.List;

/**
 * 申请单可用车型关系Service接口
 * 
 * @author hqer
 * @date 2020-05-07
 */
public interface IApplyUseCarTypeService 
{
    /**
     * 查询申请单可用车型关系
     * 
     * @param applyUseCarTypeId 申请单可用车型关系ID
     * @return 申请单可用车型关系
     */
    public ApplyUseCarType selectApplyUseCarTypeById(Long applyUseCarTypeId);

    /**
     * 查询申请单可用车型关系列表
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 申请单可用车型关系集合
     */
    public List<ApplyUseCarType> selectApplyUseCarTypeList(ApplyUseCarType applyUseCarType);

    /**
     * 新增申请单可用车型关系
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 结果
     */
    public int insertApplyUseCarType(ApplyUseCarType applyUseCarType);

    /**
     * 修改申请单可用车型关系
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 结果
     */
    public int updateApplyUseCarType(ApplyUseCarType applyUseCarType);

    /**
     * 批量删除申请单可用车型关系
     * 
     * @param applyUseCarTypeIds 需要删除的申请单可用车型关系ID
     * @return 结果
     */
    public int deleteApplyUseCarTypeByIds(Long[] applyUseCarTypeIds);

    /**
     * 删除申请单可用车型关系信息
     * 
     * @param applyUseCarTypeId 申请单可用车型关系ID
     * @return 结果
     */
    public int deleteApplyUseCarTypeById(Long applyUseCarTypeId);
}