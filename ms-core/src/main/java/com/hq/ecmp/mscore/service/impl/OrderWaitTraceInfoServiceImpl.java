package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderWaitTraceInfo;
import com.hq.ecmp.mscore.mapper.OrderWaitTraceInfoMapper;
import com.hq.ecmp.mscore.service.IOrderWaitTraceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-03-10
 */
@Service
public class OrderWaitTraceInfoServiceImpl implements IOrderWaitTraceInfoService
{
    @Autowired
    private OrderWaitTraceInfoMapper orderWaitTraceInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderWaitTraceInfo selectOrderWaitTraceInfoById(Long traceId)
    {
        return orderWaitTraceInfoMapper.selectOrderWaitTraceInfoById(traceId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderWaitTraceInfo> selectOrderWaitTraceInfoList(OrderWaitTraceInfo orderWaitTraceInfo)
    {
        return orderWaitTraceInfoMapper.selectOrderWaitTraceInfoList(orderWaitTraceInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderWaitTraceInfo(OrderWaitTraceInfo orderWaitTraceInfo)
    {
        orderWaitTraceInfo.setCreateTime(DateUtils.getNowDate());
        return orderWaitTraceInfoMapper.insertOrderWaitTraceInfo(orderWaitTraceInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderWaitTraceInfo(OrderWaitTraceInfo orderWaitTraceInfo)
    {
        orderWaitTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return orderWaitTraceInfoMapper.updateOrderWaitTraceInfo(orderWaitTraceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param traceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderWaitTraceInfoByIds(Long[] traceIds)
    {
        return orderWaitTraceInfoMapper.deleteOrderWaitTraceInfoByIds(traceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderWaitTraceInfoById(Long traceId)
    {
        return orderWaitTraceInfoMapper.deleteOrderWaitTraceInfoById(traceId);
    }
}
