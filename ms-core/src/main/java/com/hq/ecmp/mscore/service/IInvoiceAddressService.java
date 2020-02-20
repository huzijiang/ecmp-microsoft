package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.InvoiceAddress;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IInvoiceAddressService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param addressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public InvoiceAddress selectInvoiceAddressById(Long addressId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<InvoiceAddress> selectInvoiceAddressList(InvoiceAddress invoiceAddress);

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 结果
     */
    public int insertInvoiceAddress(InvoiceAddress invoiceAddress);

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 结果
     */
    public int updateInvoiceAddress(InvoiceAddress invoiceAddress);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param addressIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteInvoiceAddressByIds(Long[] addressIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param addressId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteInvoiceAddressById(Long addressId);
}
