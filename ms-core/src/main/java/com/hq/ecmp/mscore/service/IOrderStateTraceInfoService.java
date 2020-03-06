package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.dto.MessageDto;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderStateTraceInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderStateTraceInfo selectOrderStateTraceInfoById(Long traceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderStateTraceInfo> selectOrderStateTraceInfoList(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoByIds(Long[] traceIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoById(Long traceId);

    MessageDto getTraceMessage(Long userId);
}
