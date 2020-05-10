package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.DictQuery;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.EcmpDictData;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.vo.SceneListVO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 字典数据Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpDictDataService
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
     * 批量删除字典数据
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    public int deleteEcmpDictDataByIds(Long[] dictCodes);

    /**
     * 删除字典数据信息
     *
     * @param dictCode 字典数据ID
     * @return 结果
     */
    public int deleteEcmpDictDataById(Long dictCode);

    /**
     * 查询字典里面的图标
     * @param dictType
     * @return
     */
    List<SceneListVO> selectEcmpDictByType(String dictType);

    /**
     * 查询字典里面的车型图标
     * @param dictType
     * @return
     */
    List<CarTypeDTO> selectEcmpDictByCarType(String dictType);
    /**
     * 评价标签的好评
     * @return 字典数据集合
     */
    //public List<EcmpDictData> selectEcmpDictDataByTypeGOOD(DictQuery dictQuery);

    /**
     * 评价标签的差评
     * @return 字典数据集合
     */
   // public List<EcmpDictData> selectEcmpDictDataByTypeBAD();
}
