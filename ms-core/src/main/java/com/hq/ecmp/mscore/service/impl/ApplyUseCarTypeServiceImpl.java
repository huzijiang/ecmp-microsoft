package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.ApplyUseCarType;
import com.hq.ecmp.mscore.mapper.ApplyUseCarTypeMapper;
import com.hq.ecmp.mscore.service.IApplyUseCarTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 申请单可用车型关系Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-07
 */
@Service
public class ApplyUseCarTypeServiceImpl implements IApplyUseCarTypeService
{
    @Resource
    private ApplyUseCarTypeMapper applyUseCarTypeMapper;

    /**
     * 查询申请单可用车型关系
     * 
     * @param applyUseCarTypeId 申请单可用车型关系ID
     * @return 申请单可用车型关系
     */
    @Override
    public ApplyUseCarType selectApplyUseCarTypeById(Long applyUseCarTypeId)
    {
        return applyUseCarTypeMapper.selectApplyUseCarTypeById(applyUseCarTypeId);
    }

    /**
     * 查询申请单可用车型关系列表
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 申请单可用车型关系
     */
    @Override
    public List<ApplyUseCarType> selectApplyUseCarTypeList(ApplyUseCarType applyUseCarType)
    {
        return applyUseCarTypeMapper.selectApplyUseCarTypeList(applyUseCarType);
    }

    /**
     * 新增申请单可用车型关系
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 结果
     */
    @Override
    public int insertApplyUseCarType(ApplyUseCarType applyUseCarType)
    {
        return applyUseCarTypeMapper.insertApplyUseCarType(applyUseCarType);
    }

    /**
     * 修改申请单可用车型关系
     * 
     * @param applyUseCarType 申请单可用车型关系
     * @return 结果
     */
    @Override
    public int updateApplyUseCarType(ApplyUseCarType applyUseCarType)
    {
        return applyUseCarTypeMapper.updateApplyUseCarType(applyUseCarType);
    }

    /**
     * 批量删除申请单可用车型关系
     * 
     * @param applyUseCarTypeIds 需要删除的申请单可用车型关系ID
     * @return 结果
     */
    @Override
    public int deleteApplyUseCarTypeByIds(Long[] applyUseCarTypeIds)
    {
        return applyUseCarTypeMapper.deleteApplyUseCarTypeByIds(applyUseCarTypeIds);
    }

    /**
     * 删除申请单可用车型关系信息
     * 
     * @param applyUseCarTypeId 申请单可用车型关系ID
     * @return 结果
     */
    @Override
    public int deleteApplyUseCarTypeById(Long applyUseCarTypeId)
    {
        return applyUseCarTypeMapper.deleteApplyUseCarTypeById(applyUseCarTypeId);
    }
}