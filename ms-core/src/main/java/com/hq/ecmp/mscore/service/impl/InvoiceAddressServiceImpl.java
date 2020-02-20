package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.mapper.InvoiceAddressMapper;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class InvoiceAddressServiceImpl implements IInvoiceAddressService
{
    @Autowired
    private InvoiceAddressMapper invoiceAddressMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param addressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public InvoiceAddress selectInvoiceAddressById(Long addressId)
    {
        return invoiceAddressMapper.selectInvoiceAddressById(addressId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<InvoiceAddress> selectInvoiceAddressList(InvoiceAddress invoiceAddress)
    {
        return invoiceAddressMapper.selectInvoiceAddressList(invoiceAddress);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertInvoiceAddress(InvoiceAddress invoiceAddress)
    {
        invoiceAddress.setCreateTime(DateUtils.getNowDate());
        return invoiceAddressMapper.insertInvoiceAddress(invoiceAddress);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceAddress 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateInvoiceAddress(InvoiceAddress invoiceAddress)
    {
        invoiceAddress.setUpdateTime(DateUtils.getNowDate());
        return invoiceAddressMapper.updateInvoiceAddress(invoiceAddress);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param addressIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteInvoiceAddressByIds(Long[] addressIds)
    {
        return invoiceAddressMapper.deleteInvoiceAddressByIds(addressIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param addressId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteInvoiceAddressById(Long addressId)
    {
        return invoiceAddressMapper.deleteInvoiceAddressById(addressId);
    }
}
