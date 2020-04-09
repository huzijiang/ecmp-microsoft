package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderAddressInfo;
import com.hq.ecmp.mscore.mapper.OrderAddressInfoMapper;
import com.hq.ecmp.mscore.service.IOrderAddressInfoService;
import com.hq.ecmp.util.DateFormatUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-03-16
 */
@Service
public class OrderAddressInfoServiceImpl implements IOrderAddressInfoService
{
    @Autowired
    private OrderAddressInfoMapper orderAddressInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param orderAddressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderAddressInfo selectOrderAddressInfoById(Long orderAddressId)
    {
        return orderAddressInfoMapper.selectOrderAddressInfoById(orderAddressId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderAddressInfo> selectOrderAddressInfoList(OrderAddressInfo orderAddressInfo)
    {
        return orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderAddressInfo(OrderAddressInfo orderAddressInfo)
    {
        orderAddressInfo.setCreateTime(DateUtils.getNowDate());
        return orderAddressInfoMapper.insertOrderAddressInfo(orderAddressInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderAddressInfo(OrderAddressInfo orderAddressInfo)
    {
        orderAddressInfo.setUpdateTime(DateUtils.getNowDate());
        return orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param orderAddressIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderAddressInfoByIds(Long[] orderAddressIds)
    {
        return orderAddressInfoMapper.deleteOrderAddressInfoByIds(orderAddressIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param orderAddressId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderAddressInfoById(Long orderAddressId)
    {
        return orderAddressInfoMapper.deleteOrderAddressInfoById(orderAddressId);
    }

	@Override
	public OrderAddressInfo queryOrderStartAndEndInfo(OrderAddressInfo orderAddressInfo) {
		return orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfo);
	}

	@Override
	public boolean checkOrderOverTime(Long orderId) {
		OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
		orderAddressInfo.setOrderId(orderId);
		orderAddressInfo.setType("A000");
		List<OrderAddressInfo> selectOrderAddressInfoList = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
		if(null !=selectOrderAddressInfoList && selectOrderAddressInfoList.size()>0){
			OrderAddressInfo result = selectOrderAddressInfoList.get(0);
			Date actionTime = result.getActionTime();
			if(null !=actionTime && DateFormatUtils.beforeCurrentDate(actionTime)){
				return true;
			}
		}
		return false;
	}
}
