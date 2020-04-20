package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpDictData;
import com.hq.ecmp.mscore.mapper.EcmpDictDataMapper;
import com.hq.ecmp.mscore.service.IEcmpDictDataService;
import com.hq.ecmp.mscore.vo.SceneListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典数据Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpDictDataServiceImpl implements IEcmpDictDataService
{
    @Autowired
    private EcmpDictDataMapper ecmpDictDataMapper;
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合
     */
    public List<EcmpDictData> selectEcmpDictDataByType(String dictType){
        return ecmpDictDataMapper.selectEcmpDictDataByType(dictType);
    }
    /**
     * 查询字典数据
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public EcmpDictData selectEcmpDictDataById(Long dictCode)
    {
        return ecmpDictDataMapper.selectEcmpDictDataById(dictCode);
    }

    /**
     * 查询字典数据列表
     *
     * @param ecmpDictData 字典数据
     * @return 字典数据
     */
    @Override
    public List<EcmpDictData> selectEcmpDictDataList(EcmpDictData ecmpDictData)
    {
        return ecmpDictDataMapper.selectEcmpDictDataList(ecmpDictData);
    }

    /**
     * 新增字典数据
     *
     * @param ecmpDictData 字典数据
     * @return 结果
     */
    @Override
    public int insertEcmpDictData(EcmpDictData ecmpDictData)
    {
        ecmpDictData.setCreateTime(DateUtils.getNowDate());
        return ecmpDictDataMapper.insertEcmpDictData(ecmpDictData);
    }

    /**
     * 修改字典数据
     *
     * @param ecmpDictData 字典数据
     * @return 结果
     */
    @Override
    public int updateEcmpDictData(EcmpDictData ecmpDictData)
    {
        ecmpDictData.setUpdateTime(DateUtils.getNowDate());
        return ecmpDictDataMapper.updateEcmpDictData(ecmpDictData);
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public int deleteEcmpDictDataByIds(Long[] dictCodes)
    {
        return ecmpDictDataMapper.deleteEcmpDictDataByIds(dictCodes);
    }

    /**
     * 删除字典数据信息
     *
     * @param dictCode 字典数据ID
     * @return 结果
     */
    @Override
    public int deleteEcmpDictDataById(Long dictCode)
    {
        return ecmpDictDataMapper.deleteEcmpDictDataById(dictCode);
    }

    /**
     * 查询字典里面的图标
     * @param dictType
     * @return
     */
    @Override
    public List<SceneListVO> selectEcmpDictByType(String dictType) {
        return ecmpDictDataMapper.selectEcmpDictByType(dictType);
    }
    /**
     * 评价标签的好评
     * @return 字典数据集合
     */
   /* @Override
    public List<EcmpDictData> selectEcmpDictDataByTypeGOOD(){
        return ecmpDictDataMapper.selectEcmpDictDataByTypeGOOD();
    }

    *//**
     * 评价标签的差评
     * @return 字典数据集合
     *//*
    @Override
    public List<EcmpDictData> selectEcmpDictDataByTypeBAD(){
        return ecmpDictDataMapper.selectEcmpDictDataByTypeBAD();
    }*/
}
