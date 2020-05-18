package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface CostConfigInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param costId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigListResult selectCostConfigInfoById(@Param("costId") Long costId, @Param("cityCode") Integer cityCode);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigQueryDto 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigListResult> selectCostConfigInfoList(CostConfigQueryDto costConfigQueryDto);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigInfo(CostConfigInfo costConfigInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigInfo(CostConfigInfo costConfigInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param costId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigInfoById(Long costId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param costIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCostConfigInfoByIds(Long[] costIds);

    /**
     * 成本配置信息添加前判重
     * @param costConfigCarTypeInfos 车型集合
     * @param cityCode  城市code
     * @param serviceType 服务类型
     * @param rentType  包车类型
     * @return  数量
     */
    int checkDoubleByServiceTypeCityCarType(List<CostConfigCarTypeInfo> costConfigCarTypeInfos,
                                             int cityCode,String serviceType);

    /**
     * 查询成本计算的字段
     * @param costConfigQueryDto
     * @return
     */
    CostConfigInfo selectCostConfigInfo(CostConfigQueryDto costConfigQueryDto);

    /**
     * 通过城市，车型，服务类型来判断是否重复
     * @param carTypeId 车型id
     * @param cityCode 城市code
     * @param serviceType 服务类型
     * @param rentType  包车类型（服务类型是包车时使用）
     * @return
     */
    int checkDoubleByServiceTypeCityCarType(@Param("carTypeId") Long carTypeId,
                                            @Param("cityCode") int cityCode,@Param("serviceType") String serviceType,
                                            @Param("rentType") String rentType);

    /**
     * 获取条件查询的总数据
     * @param costConfigQueryDto
     * @return
     */
    int getTotalNum(CostConfigQueryDto costConfigQueryDto);
}