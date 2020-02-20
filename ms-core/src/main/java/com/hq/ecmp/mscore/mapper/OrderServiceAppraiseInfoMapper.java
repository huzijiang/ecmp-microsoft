package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderServiceAppraiseInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderServiceAppraiseInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param appraiseId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderServiceAppraiseInfo selectOrderServiceAppraiseInfoById(Long appraiseId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderServiceAppraiseInfo> selectOrderServiceAppraiseInfoList(OrderServiceAppraiseInfo orderServiceAppraiseInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderServiceAppraiseInfo(OrderServiceAppraiseInfo orderServiceAppraiseInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderServiceAppraiseInfo(OrderServiceAppraiseInfo orderServiceAppraiseInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param appraiseId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderServiceAppraiseInfoById(Long appraiseId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param appraiseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderServiceAppraiseInfoByIds(Long[] appraiseIds);
}
