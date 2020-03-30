package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface CarGroupDispatcherInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupDispatcherInfo selectCarGroupDispatcherInfoById(Long dispatcherId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupDispatcherInfo> selectCarGroupDispatcherInfoList(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDispatcherInfoById(Long dispatcherId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param dispatcherIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarGroupDispatcherInfoByIds(Long[] dispatcherIds);

    public List<Long> queryCarGroupIdList(Long userId);

    public List<Long> queryUserByCarGroup(@Param("list")List<Long> list);

    /**
     * 解绑车队调度员
     * @param carGroupId
     * @param dispatcherId
     */
    int removeCarGroupDispatcher(@Param("carGroupId") Long carGroupId,@Param("dispatcherId") Integer dispatcherId);

    /**
     * 解绑车队所有调度员
     * @param carGroupId
     * @return
     */
    int deleteCarGroupDispatcherInfoByGroupId(Long carGroupId);

    List<Long> findByCityCode(String cityCode);
}
