package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.vo.CarGroupCostVO;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface CostConfigCityInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCityInfo selectCostConfigCityInfoById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCityInfo> selectCostConfigCityInfoList(CostConfigCityInfo costConfigCityInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCityInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCityInfo(CostConfigCityInfo costConfigCityInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCityInfoById(String id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCostConfigCityInfoByIds(String[] ids);

    /**
     * 批量插入成本配置相关城市信息
     * @param cities
     * @param costId
     * @param userId
     * @param createTime
     */
    void insertCostConfigCityInfoBatch(@Param("list") List<CostConfigCityInfo> cities,@Param("costId") Long costId,
                                       @Param("userId") Long userId, @Param("createTime") Date createTime);

    /**
     * 查询此配置下是否有其他城市的信息
     * @param costId
     * @param cityCode
     * @return
     */
    int queryOtherDataByCostIdAndCityCode(@Param("costId") Long costId,@Param("cityCode") int cityCode);

    /**
     * 城市名称
     * @param cityCode
     * @return
     */
    String selectCostConfigCity(@Param("cityCode") String cityCode);

    void deleteCostConfigCityInfoByCostId(@Param("costId") Long costId);

    List<CarGroupCostVO> findGroupByCity(@Param("cityCode")String cityCode,@Param("companyId") Long companyId);

    List<CityInfo> getCostCityList(@Param("companyId") Long companyId);
}