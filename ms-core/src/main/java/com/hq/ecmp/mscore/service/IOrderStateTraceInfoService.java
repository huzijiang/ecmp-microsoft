package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.DispatchDriverInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.ReassignInfo;
import com.hq.ecmp.mscore.domain.SendCarInfo;
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

    /**
     * 订单是否改派过
     * @param orderId
     * @return
     */
    public boolean isReassignment(Long orderId);

    /**
     * 查询订单所有的流转状态
     * @param orderId
     * @return
     */
    public List<String> queryOrderAllState(Long orderId);

    /**
     * 查询驾驶员发起改派的信息
     * @return
     */
    public DispatchDriverInfo queryDispatchDriverInfo(Long orderId);
    
    /**
     * 查询调度已完成的订单派车信息
     * @param orderId
     * @return
     */
    List<SendCarInfo> queryStateInfo(Long orderId);

    MessageDto getTraceMessage(Long userId);
    
    /**
     * 司机申请改派
     * @param userId  司机编号
     * @param orderId
     * @param applyReason
     * @return
     */
    public boolean applyReassignment(Long userId,Long orderId,String applyReason);
    
    public List<ReassignInfo> queryReassignDetail(Long orderNo);
}
