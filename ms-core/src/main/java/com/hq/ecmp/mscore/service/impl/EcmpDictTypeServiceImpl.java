package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.mapper.EcmpDictTypeMapper;
import com.hq.ecmp.mscore.service.IEcmpDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典类型Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpDictTypeServiceImpl implements IEcmpDictTypeService
{
    @Autowired
    private EcmpDictTypeMapper ecmpDictTypeMapper;

    /**
     * 查询字典类型
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public EcmpDictType selectEcmpDictTypeById(Long dictId)
    {
        return ecmpDictTypeMapper.selectEcmpDictTypeById(dictId);
    }

    /**
     * 查询字典类型列表
     *
     * @param ecmpDictType 字典类型
     * @return 字典类型
     */
    @Override
    public List<EcmpDictType> selectEcmpDictTypeList(EcmpDictType ecmpDictType)
    {
        return ecmpDictTypeMapper.selectEcmpDictTypeList(ecmpDictType);
    }

    /**
     * 新增字典类型
     *
     * @param ecmpDictType 字典类型
     * @return 结果
     */
    @Override
    public int insertEcmpDictType(EcmpDictType ecmpDictType)
    {
        ecmpDictType.setCreateTime(DateUtils.getNowDate());
        return ecmpDictTypeMapper.insertEcmpDictType(ecmpDictType);
    }

    /**
     * 修改字典类型
     *
     * @param ecmpDictType 字典类型
     * @return 结果
     */
    @Override
    public int updateEcmpDictType(EcmpDictType ecmpDictType)
    {
        ecmpDictType.setUpdateTime(DateUtils.getNowDate());
        return ecmpDictTypeMapper.updateEcmpDictType(ecmpDictType);
    }

    /**
     * 批量删除字典类型
     *
     * @param dictIds 需要删除的字典类型ID
     * @return 结果
     */
    @Override
    public int deleteEcmpDictTypeByIds(Long[] dictIds)
    {
        return ecmpDictTypeMapper.deleteEcmpDictTypeByIds(dictIds);
    }

    /**
     * 删除字典类型信息
     *
     * @param dictId 字典类型ID
     * @return 结果
     */
    @Override
    public int deleteEcmpDictTypeById(Long dictId)
    {
        return ecmpDictTypeMapper.deleteEcmpDictTypeById(dictId);
    }
}
