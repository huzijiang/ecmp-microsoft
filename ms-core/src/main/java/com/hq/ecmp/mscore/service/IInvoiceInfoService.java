package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.dto.InvoiceHeaderDTO;
import com.hq.ecmp.mscore.dto.InvoiceInsertDTO;
import com.hq.ecmp.mscore.dto.InvoicePeriodDTO;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IInvoiceInfoService
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
     * @param  【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<InvoiceRecordVO> selectInvoiceInfoList();

    /**
     * 新增【发票信息】
     *
     * @param invoiceInsertDTO 【发票信息】
     * @return 结果
     */
    public Long insertInvoiceInfo(InvoiceInsertDTO invoiceInsertDTO);

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateInvoiceInfo(InvoiceInfo invoiceInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param invoiceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteInvoiceInfoByIds(Long[] invoiceIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteInvoiceInfoById(Long invoiceId);

    /**
     * 根据时间区间、开票状态查询发票信息
     */
   // public List<InvoiceInfo> selectInvoiceInfoByTimeAndState(String startTime, String endTime, String state);

    public PageResult<InvoiceRecordVO> queryAllByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO);
    /**
     * 新增发票抬头
     */
    public int insertInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO);
    /**
     * 发票抬头查询
     */
    public List<InvoiceHeaderVO> queryInvoiceHeader(Long companyId);
    /**
     * 发票抬头删除所有数据
     */
    public int deleteInvoiceHeader();
    /**
     * 发票账期关联表新增
     */
    public int addInvoicePeriod(List<InvoicePeriodDTO> invoicePeriodList);
    /**
     * 发票详情
     */
    public InvoiceDetailVO getInvoiceDetail(Long invoiceId);
    /**
     * 根据ID查询发票表信息
     */
    public InvoiceRecordVO queryInvoiceById(Long invoiceId);
    /**
     * 根据ID查询账期表信息
     */
    List<PeriodsVO> getPeriodListByInvoiceId(Long invoiceId);
}
