package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.OrderInvoiceInfo;
import com.hq.ecmp.mscore.mapper.OrderInvoiceInfoMapper;
import com.hq.ecmp.mscore.service.IOrderInvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderInvoiceInfoServiceImpl implements IOrderInvoiceInfoService
{
    @Autowired
    private OrderInvoiceInfoMapper orderInvoiceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderInvoiceInfo selectOrderInvoiceInfoById(Long invoiceId)
    {
        return orderInvoiceInfoMapper.selectOrderInvoiceInfoById(invoiceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderInvoiceInfo> selectOrderInvoiceInfoList(OrderInvoiceInfo orderInvoiceInfo)
    {
        return orderInvoiceInfoMapper.selectOrderInvoiceInfoList(orderInvoiceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderInvoiceInfo(OrderInvoiceInfo orderInvoiceInfo)
    {
        return orderInvoiceInfoMapper.insertOrderInvoiceInfo(orderInvoiceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderInvoiceInfo(OrderInvoiceInfo orderInvoiceInfo)
    {
        return orderInvoiceInfoMapper.updateOrderInvoiceInfo(orderInvoiceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param invoiceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInvoiceInfoByIds(Long[] invoiceIds)
    {
        return orderInvoiceInfoMapper.deleteOrderInvoiceInfoByIds(invoiceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInvoiceInfoById(Long invoiceId)
    {
        return orderInvoiceInfoMapper.deleteOrderInvoiceInfoById(invoiceId);
    }
}
