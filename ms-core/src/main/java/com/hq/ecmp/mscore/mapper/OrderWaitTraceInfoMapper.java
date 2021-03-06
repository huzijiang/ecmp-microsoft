package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderWaitTraceInfo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-03-10
 */
@Repository
public interface OrderWaitTraceInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderWaitTraceInfo selectOrderWaitTraceInfoById(Long traceId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderWaitTraceInfo> selectOrderWaitTraceInfoList(OrderWaitTraceInfo orderWaitTraceInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderWaitTraceInfo(OrderWaitTraceInfo orderWaitTraceInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderWaitTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderWaitTraceInfo(OrderWaitTraceInfo orderWaitTraceInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderWaitTraceInfoById(Long traceId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param traceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderWaitTraceInfoByIds(Long[] traceIds);

    /**
     * 查询等待时长
     * @param orderId
     * @return
     */
    BigDecimal selectOrderWaitingTimeById(Long orderId);
}
