package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.dto.cost.CostConfigInsertDto;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;
import com.hq.ecmp.mscore.mapper.CostConfigCarTypeInfoMapper;
import com.hq.ecmp.mscore.mapper.CostConfigCityInfoMapper;
import com.hq.ecmp.mscore.mapper.CostConfigInfoMapper;
import com.hq.ecmp.mscore.service.ICostConfigInfoService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-06
 */
@Service
public class CostConfigInfoServiceImpl implements ICostConfigInfoService
{
    @Resource
    private CostConfigInfoMapper costConfigInfoMapper;
    @Resource
    private CostConfigCityInfoMapper costConfigCityInfoMapper;
    @Resource
    private CostConfigCarTypeInfoMapper costConfigCarTypeInfoMapper;

    /**
     * 通过id查询成本配置信息
     * 
     * @param costId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CostConfigListResult selectCostConfigInfoById(Long costId)
    {
        CostConfigListResult costConfigListResult = costConfigInfoMapper.selectCostConfigInfoById(costId);
        CostConfigCarTypeInfo costConfigCarTypeInfo = new CostConfigCarTypeInfo();
        costConfigCarTypeInfo.setCostId(costConfigListResult.getCostId());
        List<CostConfigCarTypeInfo> costConfigCarTypeInfos = costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoList(costConfigCarTypeInfo);
        costConfigListResult.setCarTypes(costConfigCarTypeInfos);
        return costConfigListResult;
    }

    /**
     * 成本中心列表查询
     * @param costConfigQueryDto 成本中心列表查询条件
     * @return List<CostConfigListResult>
     */
    @Override
    public List<CostConfigListResult> selectCostConfigInfoList(CostConfigQueryDto costConfigQueryDto)
    {
        PageHelper.startPage(costConfigQueryDto.getPageNum(), costConfigQueryDto.getPageSize());
        List<CostConfigListResult> costConfigListResults = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
        for (CostConfigListResult costConfigListResult:
             costConfigListResults) {
            CostConfigCarTypeInfo costConfigCarTypeInfo = new CostConfigCarTypeInfo();
            costConfigCarTypeInfo.setCostId(costConfigListResult.getCostId());
            List<CostConfigCarTypeInfo> costConfigCarTypeInfos = costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoList(costConfigCarTypeInfo);
            costConfigListResult.setCarTypes(costConfigCarTypeInfos);
        }
        return costConfigListResults;
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostConfigInfo(CostConfigInfo costConfigInfo)
    {
        costConfigInfo.setCreateTime(DateUtils.getNowDate());
        return costConfigInfoMapper.insertCostConfigInfo(costConfigInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostConfigInfo(CostConfigInfo costConfigInfo)
    {
        costConfigInfo.setUpdateTime(DateUtils.getNowDate());
        return costConfigInfoMapper.updateCostConfigInfo(costConfigInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param costIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigInfoByIds(Long[] costIds)
    {
        return costConfigInfoMapper.deleteCostConfigInfoByIds(costIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param costId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigInfoById(Long costId)
    {
        return costConfigInfoMapper.deleteCostConfigInfoById(costId);
    }

    /**
     * 录入成本中心配置信息
     * @param costConfigDto 创建成本设置入参model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCostConfig(CostConfigInsertDto costConfigDto, Long userId) throws Exception {
        CostConfigInfo costConfigInfo = new CostConfigInfo();
        BeanUtils.copyProperties(costConfigDto,costConfigInfo);
        costConfigInfo.setCreateBy(String.valueOf(userId));
        ((ICostConfigInfoService)AopContext.currentProxy()).insertCostConfigInfo(costConfigInfo);
        Long costId = costConfigInfo.getCostId();
        if(costId == null){
            throw new  Exception("新增失败");
        }
        costConfigCityInfoMapper.insertCostConfigCityInfoBatch(costConfigDto.getCities(),costId,userId,DateUtils.getNowDate());
        costConfigCarTypeInfoMapper.insertCostConfigCarTypeInfoBatch(costConfigDto.getCarTypes(),costId,userId,DateUtils.getNowDate());
    }

    /**
     * 更新成本中心配置信息
     * 1.基础表信息更新
     * 2.车型辅表根据成本配置id删除后，重新插入
     * @param costConfigListResult 成本设置更新入参条件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCostConfig(CostConfigListResult costConfigListResult,Long userId) throws Exception {
        CostConfigInfo costConfigInfo = new CostConfigInfo();
        costConfigInfo.setUpdateBy(String.valueOf(userId));
        BeanUtils.copyProperties(costConfigListResult, costConfigInfo);
        int i = ((ICostConfigInfoService) AopContext.currentProxy()).updateCostConfigInfo(costConfigInfo);
        if(i==0){
            throw new Exception("更新失败");
        }
        Long costId = costConfigListResult.getCostId();
        costConfigCarTypeInfoMapper.deleteCostConfigByCostId(costId);
        costConfigCarTypeInfoMapper.insertCostConfigCarTypeInfoBatch(costConfigListResult.getCarTypes(), costId,userId ,DateUtils.getNowDate() );
    }

    /**
     * 通过成本城市附表id删除城市相关配置
     * @param costConfigCityId  成本城市附表id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCostConfigByCostCityId(Long costConfigCityId,Long costId,int cityCode) {
        int i = costConfigCityInfoMapper.queryOtherDataByCostIdAndCityCode(costId, cityCode);
        if(i==0){
            costConfigInfoMapper.deleteCostConfigInfoById(costId);
        }
        costConfigCityInfoMapper.deleteCostConfigCityInfoById(String.valueOf(costConfigCityId));
    }

    /**
     *
     * @param costConfigQueryDto  判重条件
     * @return 数量
     */
    @Override
    public int checkDoubleByServiceTypeCityCarType(CostConfigQueryDto costConfigQueryDto) {
        int count;
        count = costConfigInfoMapper.checkDoubleByServiceTypeCityCarType(
                costConfigQueryDto.getCarType(),costConfigQueryDto.getCityCode(),costConfigQueryDto.getServiceType());
        return count;
    }
}