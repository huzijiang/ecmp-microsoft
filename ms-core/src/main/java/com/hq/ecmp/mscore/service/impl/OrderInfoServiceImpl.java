package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderInfoServiceImpl implements IOrderInfoService
{
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private IJourneyInfoService iJourneyInfoService;

    @Resource
    private IJourneyNodeInfoService iJourneyNodeInfoService;

    @Resource
    private IOrderInfoService iOrderInfoService;

    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderInfo selectOrderInfoById(Long orderId)
    {
        return orderInfoMapper.selectOrderInfoById(orderId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo)
    {
        return orderInfoMapper.selectOrderInfoList(orderInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderInfo(OrderInfo orderInfo)
    {
        orderInfo.setCreateTime(DateUtils.getNowDate());
        return orderInfoMapper.insertOrderInfo(orderInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderInfo(OrderInfo orderInfo)
    {
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        return orderInfoMapper.updateOrderInfo(orderInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoByIds(Long[] orderIds)
    {
        return orderInfoMapper.deleteOrderInfoByIds(orderIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoById(Long orderId)
    {
        return orderInfoMapper.deleteOrderInfoById(orderId);
    }


    /**
     * 获取乘客订单列表
     * @param userId
     * @return
     */
    public List<OrderListInfo>  getOrderList(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<OrderListInfo> orderList = orderInfoMapper.getOrderList(userId);
        return orderList;
    }

    /**
     * 订单状态修改方法
     * @param orderId
     * @param updateState
     * @return
     */
    public  int insertOrderStateTrace(String orderId,String updateState,String userId){
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(updateState);
        orderStateTraceInfo.setContent(null);
        orderStateTraceInfo.setCreateBy(userId);
        int i = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        return  i;
    }
}
