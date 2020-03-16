package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpDictData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典数据Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpDictDataMapper
{
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合
     */
    public List<EcmpDictData> selectEcmpDictDataByType(String dictType);
    /**
     * 查询字典数据
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public EcmpDictData selectEcmpDictDataById(Long dictCode);

    /**
     * 查询字典数据列表
     *
     * @param ecmpDictData 字典数据
     * @return 字典数据集合
     */
    public List<EcmpDictData> selectEcmpDictDataList(EcmpDictData ecmpDictData);

    /**
     * 新增字典数据
     *
     * @param ecmpDictData 字典数据
     * @return 结果
     */
    public int insertEcmpDictData(EcmpDictData ecmpDictData);

    /**
     * 修改字典数据
     *
     * @param ecmpDictData 字典数据
     * @return 结果
     */
    public int updateEcmpDictData(EcmpDictData ecmpDictData);

    /**
     * 删除字典数据
     *
     * @param dictCode 字典数据ID
     * @return 结果
     */
    public int deleteEcmpDictDataById(Long dictCode);

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpDictDataByIds(Long[] dictCodes);

    /**
     * 评价标签的好评
     * @return 字典数据集合
     */
    public List<EcmpDictData> selectEcmpDictDataByTypeGOOD();

    /**
     * 评价标签的差评
     * @return 字典数据集合
     */
    public List<EcmpDictData> selectEcmpDictDataByTypeBAD();
}
