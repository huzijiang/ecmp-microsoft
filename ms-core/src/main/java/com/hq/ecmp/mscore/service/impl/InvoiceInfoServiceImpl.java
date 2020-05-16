package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.domain.OrderInvoiceInfo;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.dto.InvoiceHeaderDTO;
import com.hq.ecmp.mscore.dto.InvoiceInsertDTO;
import com.hq.ecmp.mscore.dto.InvoicePeriodDTO;
import com.hq.ecmp.mscore.mapper.InvoiceInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.MailUtils;
import com.hq.ecmp.mscore.mapper.OrderInvoiceInfoMapper;
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
    @Autowired
    private OrderInvoiceInfoMapper orderInvoiceInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;

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
     * 新增【发票信息】
     *
     * @param invoiceInsertDTO 【发票信息】
     * @return 结果
     */
    public Long insertInvoiceInfo(InvoiceInsertDTO invoiceInsertDTO)
    {
        invoiceInsertDTO.setCreateTime(DateUtils.getNowDate());
        invoiceInsertDTO.setStatus("11");
        return invoiceInfoMapper.insertInvoiceInfo(invoiceInsertDTO);
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

    public PageResult<InvoiceRecordVO> queryAllByTimeState(InvoiceByTimeStateDTO invoiceByTimeStateDTO){
        PageHelper.startPage(invoiceByTimeStateDTO.getPageNum(),invoiceByTimeStateDTO.getPageSize());
        List<InvoiceRecordVO> invoiceRecordVOS = invoiceInfoMapper.queryAllByTimeState(invoiceByTimeStateDTO);
        Long count=invoiceInfoMapper.queryCountByTimeState(invoiceByTimeStateDTO);
        return new PageResult<>(count,invoiceRecordVOS);
    }
    /**
     * 新增发票抬头
     */
    public int insertInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO){
        invoiceHeaderDTO.setCreateTime(DateUtils.getNowDate());
        return invoiceInfoMapper.insertInvoiceHeader(invoiceHeaderDTO);
    }
    /**
     * 发票抬头查询
     */
    public List<InvoiceHeaderVO> queryInvoiceHeader(Long companyId){
        return invoiceInfoMapper.queryInvoiceHeader(companyId);
    }
    /**
     * 发票抬头删除所有数据
     */
    public int deleteInvoiceHeader(){
        return invoiceInfoMapper.deleteInvoiceHeader();
    }
    /**
     * 发票账期关联表新增
     */
    public int addInvoicePeriod(List<InvoicePeriodDTO> invoicePeriodList){
        return invoiceInfoMapper.addInvoicePeriod(invoicePeriodList);
    }
    public int addInvoice(List<OrderInvoiceInfo> invoicePeriodList){
        return orderInvoiceInfoMapper.addInvoicePeriod(invoicePeriodList);
    }
    /**
     * 发票详情
     */
    public InvoiceDetailVO getInvoiceDetail(Long invoiceId){
        InvoiceRecordVO invoiceRecord = invoiceInfoMapper.queryInvoiceById(invoiceId);
        InvoiceDetailVO ivoice = new InvoiceDetailVO();
        ivoice.setType(invoiceRecord.getType());
        ivoice.setAmount(invoiceRecord.getAmount());
        ivoice.setCreateTime(invoiceRecord.getCreateTime());
        ivoice.setStatus(invoiceRecord.getStatus());
        ivoice.setHeader(invoiceRecord.getHeader());
        ivoice.setAcceptAddress(invoiceRecord.getAcceptAddress());
        ivoice.setTin(invoiceRecord.getTin());
        ivoice.setContent(invoiceRecord.getContent());
        ivoice.setBankName(invoiceRecord.getBankName());
        ivoice.setRegistedAddress(invoiceRecord.getRegistedAddress());
        ivoice.setBankCardNo(invoiceRecord.getBankCardNo());
        ivoice.setTelephone(invoiceRecord.getTelephone());
        ivoice.setEmail(invoiceRecord.getEmail());
        List<PeriodsVO> periods=invoiceInfoMapper.getPeriodListByInvoiceId(invoiceId);
        ivoice.setPeriods(periods);

        return ivoice;
    }
    /**
     * 根据ID查询发票表信息
     */
    public InvoiceRecordVO queryInvoiceById(Long invoiceId){
        return invoiceInfoMapper.queryInvoiceById(invoiceId);
    }

    /**
     * 根据ID查询账期表信息
     */
    public List<PeriodsVO> getPeriodListByInvoiceId(Long invoiceId){
        return invoiceInfoMapper.getPeriodListByInvoiceId(invoiceId);
    }

    /***
     * 发票重发
     * @param invoiceId
     * @param mailboxes
     * @throws Exception
     */
    @Override
    public void reissueofInvoice(Long invoiceId, String mailboxes,String toResend) throws Exception {
        InvoiceRecordVO invoiceRecordVO =invoiceInfoMapper.queryInvoiceById(invoiceId);
        String path = invoiceRecordVO.getInvoiceUrl();
        String message="请查收您的发票，感谢关注红旗智行科技";
        if("1".equals(toResend)){//是否发送行程（0否1是）
            OrderInvoiceInfo orderInvoiceInfo = new OrderInvoiceInfo();
            orderInvoiceInfo.setInvoiceId(invoiceId);
            List<OrderInvoiceInfo> list  = orderInvoiceInfoMapper.selectOrderInvoiceInfoList(orderInvoiceInfo);
            for(OrderInvoiceInfo data :list ){
                List<InvoiceAbleItineraryData> key =  journeyInfoMapper.getInvoiceAbleItineraryHistoryKey(data.getAccountId());
                message = message+key.get(0).getActionTime()+"              订单"+key.get(0).getAmount()+"元\r\n" +
                        key.get(0).getAddress()+"\r\n"+key.get(1).getAddress()+"\r\n";
            }
        }
        MailUtils.sendMail(mailboxes,"您有一张发票请查收",message,path,invoiceId+".pdf");

    }
}
