package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpDictType;

import java.util.List;

/**
 * 字典类型Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpDictTypeService
{
    /**
     * 查询字典类型
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public EcmpDictType selectEcmpDictTypeById(Long dictId);

    /**
     * 查询字典类型列表
     *
     * @param ecmpDictType 字典类型
     * @return 字典类型集合
     */
    public List<EcmpDictType> selectEcmpDictTypeList(EcmpDictType ecmpDictType);

    /**
     * 新增字典类型
     *
     * @param ecmpDictType 字典类型
     * @return 结果
     */
    public int insertEcmpDictType(EcmpDictType ecmpDictType);

    /**
     * 修改字典类型
     *
     * @param ecmpDictType 字典类型
     * @return 结果
     */
    public int updateEcmpDictType(EcmpDictType ecmpDictType);

    /**
     * 批量删除字典类型
     *
     * @param dictIds 需要删除的字典类型ID
     * @return 结果
     */
    public int deleteEcmpDictTypeByIds(Long[] dictIds);

    /**
     * 删除字典类型信息
     *
     * @param dictId 字典类型ID
     * @return 结果
     */
    public int deleteEcmpDictTypeById(Long dictId);
}
