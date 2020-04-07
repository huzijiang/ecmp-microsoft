package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.dto.InvoiceAddUpdateDTO;
import com.hq.ecmp.mscore.dto.InvoiceAddressDTO;
import com.hq.ecmp.mscore.mapper.InvoiceAddressMapper;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.vo.InvoiceAddVO;
import com.hq.ecmp.mscore.vo.PageResult;
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

   /* @Override
    public PageResult<CarGroupListVO> selectInvoiceAddressList(Integer pageNum, Integer pageSize, String search) {
        return null;
    }*/

    /**
     * 查询【请填写功能名称】列表
     *
     * @param  【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<InvoiceAddVO> selectInvoiceAddressList()
    {
        return invoiceAddressMapper.selectInvoiceAddressList();
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceAddressDTO 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertInvoiceAddress(InvoiceAddressDTO invoiceAddressDTO)
    {
        invoiceAddressDTO.setCreateTime(DateUtils.getNowDate());
        return invoiceAddressMapper.insertInvoiceAddress(invoiceAddressDTO);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceAddUpdateDTO 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateInvoiceAddress(InvoiceAddUpdateDTO invoiceAddUpdateDTO)
    {
        invoiceAddUpdateDTO.setUpdateTime(DateUtils.getNowDate());
        return invoiceAddressMapper.updateInvoiceAddress(invoiceAddUpdateDTO);
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
