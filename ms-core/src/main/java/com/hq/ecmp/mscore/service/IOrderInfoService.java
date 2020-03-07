package com.hq.ecmp.mscore.service;


import com.github.pagehelper.PageInfo;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.domain.OrderDriverListInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderInfoService {
    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderInfo selectOrderInfoById(Long orderId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderInfo(OrderInfo orderInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] orderIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long orderId);

    /**
     * 我的行程订单列表
     *
     * @param userId
     * @return
     */
    public List<OrderListInfo> getOrderList(Long userId, int pageNum, int pageSize);

    /**
     * 订单轨迹表添加通用方法
     * @param orderId
     * @param updateState
     * @param userId
     * @return
     */
    public  int insertOrderStateTrace(String orderId,String updateState,String userId);
    
    
    /**
     * 查询所有待调度的订单(包含待改派)
     * @return
     */
    public List<DispatchOrderInfo> queryWaitDispatchList();
    
    /**
     * 查询所有已完成调度的订单
     * @return
     */
    public List<DispatchOrderInfo> queryCompleteDispatchOrder();

    /**
     *  通过用户id查询司机的任务列表
     * @param userId
     * @return
     */
    public  List<OrderDriverListInfo> getDriverOrderList(Long userId,int pageNum, int pageSize);
    
    /**
     * 查询待调单的订单详情(包含待改派的)
     * @param orderId
     * @return
     */
    public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId);
}

