package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderViaInfo;
import com.hq.ecmp.mscore.mapper.OrderViaInfoMapper;
import com.hq.ecmp.mscore.service.IOrderViaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-03-12
 */
@Service
public class OrderViaInfoServiceImpl implements IOrderViaInfoService
{
    @Autowired
    private OrderViaInfoMapper orderViaInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param viaId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderViaInfo selectOrderViaInfoById(Long viaId)
    {
        return orderViaInfoMapper.selectOrderViaInfoById(viaId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderViaInfo> selectOrderViaInfoList(OrderViaInfo orderViaInfo)
    {
        return orderViaInfoMapper.selectOrderViaInfoList(orderViaInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderViaInfo(OrderViaInfo orderViaInfo)
    {
        orderViaInfo.setCreateTime(DateUtils.getNowDate());
        return orderViaInfoMapper.insertOrderViaInfo(orderViaInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderViaInfo(OrderViaInfo orderViaInfo)
    {
        orderViaInfo.setUpdateTime(DateUtils.getNowDate());
        return orderViaInfoMapper.updateOrderViaInfo(orderViaInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param viaIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderViaInfoByIds(Long[] viaIds)
    {
        return orderViaInfoMapper.deleteOrderViaInfoByIds(viaIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param viaId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderViaInfoById(Long viaId)
    {
        return orderViaInfoMapper.deleteOrderViaInfoById(viaId);
    }

    @Override
    public void insertOrderViaInfoBatch(List<OrderViaInfo> list) {
        orderViaInfoMapper.insertOrderViaInfoBatch(list);
    }
}
