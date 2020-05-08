package com.hq.ecmp.mscore.mapper;

import com.hq.api.system.domain.SysRoleMenu;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.dto.InvoiceHeaderDTO;
import com.hq.ecmp.mscore.dto.InvoiceInsertDTO;
import com.hq.ecmp.mscore.dto.InvoicePeriodDTO;
import com.hq.ecmp.mscore.vo.InvoiceDetailVO;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
import com.hq.ecmp.mscore.vo.PeriodsVO;
import com.sun.jna.platform.win32.WinDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface InvoiceInfoMapper
{
    /**
     * 查询【发票信息】
     *
     * @param invoiceId 【发票信息】ID
     * @return 【发票信息】
     */
    public InvoiceInfo selectInvoiceInfoById(Long invoiceId);

    /**
     * 查询发票记录列表
     *
     * @param  【发票信息】
     * @return 【发票信息】集合
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
     * 修改【发票信息】
     *
     * @param invoiceInfo 【发票信息】
     * @return 结果
     */
    public int updateInvoiceInfo(InvoiceInfo invoiceInfo);

    /**
     * 删除【发票信息】
     *
     * @param invoiceId 【发票信息】ID
     * @return 结果
     */
    public int deleteInvoiceInfoById(Long invoiceId);

    /**
     * 批量删除【发票信息】
     *
     * @param invoiceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInvoiceInfoByIds(Long[] invoiceIds);
    /**
     * 根据时间区间、开票状态查询发票信息
     */

    public List<InvoiceRecordVO> queryAllByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO);
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
    InvoiceDetailVO getInvoiceDetail(Long invoiceId);
    /**
     * 根据ID查询发票表信息
     */
    InvoiceRecordVO queryInvoiceById(Long invoiceId);
   /**
   * 根据ID查询账期表信息
   */
    List<PeriodsVO> getPeriodListByInvoiceId(Long invoiceId);

    Long queryCountByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO);
}
