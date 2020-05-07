package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.dto.cost.CostConfigInsertDto;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-05-06
 */
public interface ICostConfigInfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param costId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    CostConfigListResult selectCostConfigInfoById(Long costId);


    /**
     * 成本配置信息列表查询
     * @param costConfigInfo
     * @return
     */
    public List<CostConfigListResult> selectCostConfigInfoList(CostConfigQueryDto costConfigInfo);

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
     * 批量删除【请填写功能名称】
     * 
     * @param costIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigInfoByIds(Long[] costIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param costId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigInfoById(Long costId);

    /**
     * 录入成本中心配置信息
     * @param costConfigDto
     * @param userId
     * @throws Exception
     */
    void  createCostConfig(CostConfigInsertDto costConfigDto, Long userId) throws Exception;

    /**
     * 成本设置
     * @param costConfigListResult
     * @param userId
     * @throws  Exception
     */
    void updateCostConfig(CostConfigListResult costConfigListResult,Long userId) throws Exception;

    /**
     * 通过成本设置城市附表id删除成本设置数据
     * @param costConfigCityId
     * @param cityCode
     * @param costId
     */
    void deleteCostConfigByCostCityId(Long costConfigCityId,Long costId,int cityCode);

    /**
     * 通过城市code，服务类型，车型id来判重
     * @param costConfigQueryDto  判重条件
     * @return
     */
    int checkDoubleByServiceTypeCityCarType(CostConfigQueryDto costConfigQueryDto);
}