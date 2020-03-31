package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.mapper.InvoiceInfoMapper;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class InvoiceInfoServiceImpl implements IInvoiceInfoService
{
    @Autowired
    private InvoiceInfoMapper invoiceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public InvoiceInfo selectInvoiceInfoById(Long invoiceId)
    {
        return invoiceInfoMapper.selectInvoiceInfoById(invoiceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<InvoiceRecordVO> selectInvoiceInfoList()
    {
        return invoiceInfoMapper.selectInvoiceInfoList();
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertInvoiceInfo(InvoiceInfo invoiceInfo)
    {
        invoiceInfo.setCreateTime(DateUtils.getNowDate());
        return invoiceInfoMapper.insertInvoiceInfo(invoiceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param invoiceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateInvoiceInfo(InvoiceInfo invoiceInfo)
    {
        invoiceInfo.setUpdateTime(DateUtils.getNowDate());
        return invoiceInfoMapper.updateInvoiceInfo(invoiceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param invoiceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteInvoiceInfoByIds(Long[] invoiceIds)
    {
        return invoiceInfoMapper.deleteInvoiceInfoByIds(invoiceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param invoiceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteInvoiceInfoById(Long invoiceId)
    {
        return invoiceInfoMapper.deleteInvoiceInfoById(invoiceId);
    }


    /**
     * 根据时间区间、开票状态查询发票信息
     */
   /* public List<InvoiceInfo> selectInvoiceInfoByTimeAndState(String startTime, String endTime, String state){
        return invoiceInfoMapper.selectInvoiceInfoListByTimeAndState(startTime,endTime,state);
    }*/

    public List<InvoiceRecordVO> queryAllByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO){
        return invoiceInfoMapper.queryAllByTimeState(invoiceByTimeStateDTO);
    }
    /**
     * 新增发票抬头
     */
    public int insertInvoiceHeader(InvoiceHeaderVO invoiceHeaderVO){
        return invoiceInfoMapper.insertInvoiceHeader(invoiceHeaderVO);
    }

}
