package com.hq.ecmp.mscore.service;
import com.hq.ecmp.mscore.dto.InvoiceAddUpdateDTO;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.vo.InvoiceAddVO;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.dto.InvoiceAddressDTO;
import com.hq.ecmp.mscore.vo.PageResult;


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
     * @param  【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public PageResult<InvoiceAddVO> selectInvoiceAddressList(PageRequest pageRequest,Long companyId);

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceAddressDTO 【请填写功能名称】
     * @return 结果
     */
    public int insertInvoiceAddress(InvoiceAddressDTO invoiceAddressDTO);

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceAddUpdateDTO 【请填写功能名称】
     * @return 结果
     */
    public int updateInvoiceAddress(InvoiceAddUpdateDTO invoiceAddUpdateDTO);

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
