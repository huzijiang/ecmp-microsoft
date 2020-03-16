package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.InvoiceInfo;
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
     * 查询【发票信息】列表
     *
     * @param invoiceInfo 【发票信息】
     * @return 【发票信息】集合
     */
    public List<InvoiceInfo> selectInvoiceInfoList(InvoiceInfo invoiceInfo);

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
    public List<InvoiceInfo> selectInvoiceInfoListByTimeAndState(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("state") String state);

}
