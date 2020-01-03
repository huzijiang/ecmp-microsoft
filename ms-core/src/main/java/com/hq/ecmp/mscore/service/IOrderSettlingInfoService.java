package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderSettlingInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderSettlingInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param billId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderSettlingInfo selectOrderSettlingInfoById(Long billId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderSettlingInfo> selectOrderSettlingInfoList(OrderSettlingInfo orderSettlingInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param billIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderSettlingInfoByIds(Long[] billIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param billId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderSettlingInfoById(Long billId);
}
