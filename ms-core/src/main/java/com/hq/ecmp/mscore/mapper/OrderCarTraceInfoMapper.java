package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderCarTraceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderCarTraceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderCarTraceInfo selectOrderCarTraceInfoById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderCarTraceInfo> selectOrderCarTraceInfoList(OrderCarTraceInfo orderCarTraceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderCarTraceInfo(OrderCarTraceInfo orderCarTraceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderCarTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderCarTraceInfo(OrderCarTraceInfo orderCarTraceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderCarTraceInfoById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderCarTraceInfoByIds(String[] ids);
}
