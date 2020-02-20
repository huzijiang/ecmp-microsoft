package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.InvoiceInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface InvoiceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public InvoiceInfo selectInvoiceInfoById(Long invoiceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<InvoiceInfo> selectInvoiceInfoList(InvoiceInfo invoiceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertInvoiceInfo(InvoiceInfo invoiceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateInvoiceInfo(InvoiceInfo invoiceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteInvoiceInfoById(Long invoiceId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param invoiceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInvoiceInfoByIds(Long[] invoiceIds);
}
