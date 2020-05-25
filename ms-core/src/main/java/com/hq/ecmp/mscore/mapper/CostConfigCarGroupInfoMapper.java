package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CostConfigCarGroupInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-24
 */
public interface CostConfigCarGroupInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CostConfigCarGroupInfo selectCostConfigCarGroupInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostConfigCarGroupInfo> selectCostConfigCarGroupInfoList(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigCarGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCostConfigCarGroupInfo(CostConfigCarGroupInfo costConfigCarGroupInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCostConfigCarGroupInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCostConfigCarGroupInfoByIds(Long[] ids);

    /**
     * 批量插入
     * @param cities
     * @param costId
     * @param userId
     * @param createTime
     */
    void insertCostConfigCarGroupInfoBatch(@Param("list") List<CostConfigCarGroupInfo> cities, @Param("costId") Long costId,
                                       @Param("userId") Long userId, @Param("createTime") Date createTime);

    void deleteCostConfigCarGroupInfoByCostId(Long costId);
}
