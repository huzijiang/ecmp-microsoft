package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-05-25
 */
public interface IOrderDispatcheDetailInfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderDispatcheDetailInfo selectOrderDispatcheDetailInfoById(Integer dispatchId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderDispatcheDetailInfo> selectOrderDispatcheDetailInfoList(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param dispatchIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderDispatcheDetailInfoByIds(Integer[] dispatchIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderDispatcheDetailInfoById(Integer dispatchId);
}