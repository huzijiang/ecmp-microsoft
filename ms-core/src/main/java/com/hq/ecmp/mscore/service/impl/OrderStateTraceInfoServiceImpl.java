package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderStateTraceInfoServiceImpl implements IOrderStateTraceInfoService
{
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderStateTraceInfo selectOrderStateTraceInfoById(Long traceId)
    {
        return orderStateTraceInfoMapper.selectOrderStateTraceInfoById(traceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderStateTraceInfo> selectOrderStateTraceInfoList(OrderStateTraceInfo orderStateTraceInfo)
    {
        return orderStateTraceInfoMapper.selectOrderStateTraceInfoList(orderStateTraceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo)
    {
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        return orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo)
    {
        orderStateTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return orderStateTraceInfoMapper.updateOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderStateTraceInfoByIds(Long[] traceIds)
    {
        return orderStateTraceInfoMapper.deleteOrderStateTraceInfoByIds(traceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderStateTraceInfoById(Long traceId)
    {
        return orderStateTraceInfoMapper.deleteOrderStateTraceInfoById(traceId);
    }
}
