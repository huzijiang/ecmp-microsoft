package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface CostConfigCarTypeInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCarTypeInfo selectCostConfigCarTypeInfoById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCarTypeInfo> selectCostConfigCarTypeInfoList(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCarTypeInfo(CostConfigCarTypeInfo costConfigCarTypeInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarTypeInfoById(String id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCostConfigCarTypeInfoByIds(String[] ids);

    /**
     * 批量插入成本配置车型信息
     */
    void insertCostConfigCarTypeInfoBatch(@Param("list") List<CostConfigCarTypeInfo> carTypes,@Param("costId") Long costId,
                                          @Param("userId") Long userId,@Param("createTime") Date createTime);

    /**
     * 通过成本设置id，删除对应的车型信息
     * @param costId
     */
    void deleteCostConfigByCostId(Long costId);
}