package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.OrderCarTraceInfo;
import com.hq.ecmp.mscore.mapper.OrderCarTraceInfoMapper;
import com.hq.ecmp.mscore.service.IOrderCarTraceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderCarTraceInfoServiceImpl implements IOrderCarTraceInfoService
{
    @Autowired
    private OrderCarTraceInfoMapper orderCarTraceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderCarTraceInfo selectOrderCarTraceInfoById(String id)
    {
        return orderCarTraceInfoMapper.selectOrderCarTraceInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderCarTraceInfo> selectOrderCarTraceInfoList(OrderCarTraceInfo orderCarTraceInfo)
    {
        return orderCarTraceInfoMapper.selectOrderCarTraceInfoList(orderCarTraceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderCarTraceInfo(OrderCarTraceInfo orderCarTraceInfo)
    {
        return orderCarTraceInfoMapper.insertOrderCarTraceInfo(orderCarTraceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderCarTraceInfo(OrderCarTraceInfo orderCarTraceInfo)
    {
        return orderCarTraceInfoMapper.updateOrderCarTraceInfo(orderCarTraceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderCarTraceInfoByIds(String[] ids)
    {
        return orderCarTraceInfoMapper.deleteOrderCarTraceInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderCarTraceInfoById(String id)
    {
        return orderCarTraceInfoMapper.deleteOrderCarTraceInfoById(id);
    }
}
