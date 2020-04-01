package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
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
     * @param invoiceInfo 【发票信息】
     * @return 结果
     */
    public int insertInvoiceInfo(InvoiceInfo invoiceInfo);

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
    //public List<InvoiceInfo> selectInvoiceInfoListByTimeAndState(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("state") String state);
    public List<InvoiceRecordVO> queryAllByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO);
    /**
     * 新增发票抬头
     */
    public int insertInvoiceHeader(InvoiceHeaderVO invoiceHeaderVO);

}
