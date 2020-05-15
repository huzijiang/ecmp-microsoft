package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderInvoiceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderInvoiceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderInvoiceInfo selectOrderInvoiceInfoById(Long invoiceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderInvoiceInfo> selectOrderInvoiceInfoList(OrderInvoiceInfo orderInvoiceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderInvoiceInfo(OrderInvoiceInfo orderInvoiceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInvoiceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderInvoiceInfo(OrderInvoiceInfo orderInvoiceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInvoiceInfoById(Long invoiceId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param invoiceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderInvoiceInfoByIds(Long[] invoiceIds);


    /***
     *
     * @param list
     * @return
     */
    int addInvoicePeriod(List<OrderInvoiceInfo> list );
}
