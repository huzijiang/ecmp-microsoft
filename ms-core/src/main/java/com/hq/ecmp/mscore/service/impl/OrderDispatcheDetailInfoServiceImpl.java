package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;
import com.hq.ecmp.mscore.mapper.OrderDispatcheDetailInfoMapper;
import com.hq.ecmp.mscore.service.IOrderDispatcheDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-25
 */
@Service
public class OrderDispatcheDetailInfoServiceImpl implements IOrderDispatcheDetailInfoService
{
    @Autowired
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderDispatcheDetailInfo selectOrderDispatcheDetailInfoById(Integer dispatchId)
    {
        return orderDispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoById(dispatchId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderDispatcheDetailInfo> selectOrderDispatcheDetailInfoList(OrderDispatcheDetailInfo orderDispatcheDetailInfo)
    {
        return orderDispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo)
    {
        orderDispatcheDetailInfo.setCreateTime(DateUtils.getNowDate());
        return orderDispatcheDetailInfoMapper.insertOrderDispatcheDetailInfo(orderDispatcheDetailInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo)
    {
        orderDispatcheDetailInfo.setUpdateTime(DateUtils.getNowDate());
        return orderDispatcheDetailInfoMapper.updateOrderDispatcheDetailInfo(orderDispatcheDetailInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param dispatchIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderDispatcheDetailInfoByIds(Integer[] dispatchIds)
    {
        return orderDispatcheDetailInfoMapper.deleteOrderDispatcheDetailInfoByIds(dispatchIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderDispatcheDetailInfoById(Integer dispatchId)
    {
        return orderDispatcheDetailInfoMapper.deleteOrderDispatcheDetailInfoById(dispatchId);
    }
}