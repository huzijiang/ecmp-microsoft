package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.ms.api.dto.invoice.InvoiceDto;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.vo.InvoiceDetailVO;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 发票地址
 * @author shixin
 * @date 2020-3-6
 *
 */
 @RestController
 @RequestMapping("/invoice")
public class InvoiceInfoController {

    @Autowired
    private IInvoiceInfoService invoiceInfoService;
    /**I
     * 根据时间区间、开票状态查询发票记录信息
     * @param invoiceByTimeStateDTO
     * @return
     */
    @Log(title = "财务模块", content = "发票记录列表查询",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceInfoList",notes = "发票记录列表查询",httpMethod = "POST")
    @PostMapping("/getInvoiceInfoList")
    public ApiResponse<PageResult<InvoiceRecordVO>> getInvoiceInfoList(@RequestBody InvoiceByTimeStateDTO invoiceByTimeStateDTO){
        PageResult<InvoiceRecordVO> invoiceInfoList = invoiceInfoService.queryAllByTimeState(invoiceByTimeStateDTO);
        return ApiResponse.success(invoiceInfoList);
    }
    /**
     * 发票信息新增
     * @param invoiceDTO
     * @return
     */
    @Log(title = "财务模块",content = "新增发票信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "invoiceInfoCommit",notes = "新增发票信息",httpMethod = "POST")
    @PostMapping("/invoiceInfoCommit")
     public ApiResponse invoiceInfoCommit(@RequestBody InvoiceDTO invoiceDTO){
        //发票信息
        InvoiceInsertDTO invoiceInsertDTO = new InvoiceInsertDTO();
        invoiceInsertDTO.setType(invoiceDTO.getType());
        invoiceInsertDTO.setHeader(invoiceDTO.getHeader());
        invoiceInsertDTO.setTin(invoiceDTO.getTin());
        invoiceInsertDTO.setContent(invoiceDTO.getContent());
        invoiceInsertDTO.setAmount(invoiceDTO.getAmount());
        invoiceInsertDTO.setAcceptAddress(invoiceDTO.getAcceptAddress());
        try {
         invoiceInfoService.insertInvoiceInfo(invoiceInsertDTO);
         Long invoiceId = invoiceInsertDTO.getInvoiceId();
         System.out.println("新增发票返回ID："+ invoiceId);
        //发票账期
        List<InvoicePeriodDTO> list = new ArrayList<InvoicePeriodDTO>();
        Long[] periodIds =invoiceDTO.getPeriodId();
        System.out.println("前端返回账期ID："+ periodIds);
        for (Long periodId : periodIds)
           {
            InvoicePeriodDTO invoicePer = new InvoicePeriodDTO();
            invoicePer.setInvoiceId(invoiceId);
            invoicePer.setPeriodId(periodId);
            list.add(invoicePer);
            }
            if(list.size()>0){
            invoiceInfoService.addInvoicePeriod(list);
          //  return ApiResponse.success("成功开发票");
           }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("开发票成功");
    }


    /**
     * 发票信息详情
     * @param insertDTO
     * @return
     */
    @Log(title = "财务模块",content = "发票信息详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceInfoDetail",notes = "发票信息详情",httpMethod ="POST")
    @PostMapping("/getInvoiceInfoDetail")
    public ApiResponse<InvoiceDetailVO> getInvoiceInfoDetail(@RequestBody InvoiceInsertDTO insertDTO){
        InvoiceDetailVO invoiceInfoDetail = invoiceInfoService.getInvoiceDetail(insertDTO.getInvoiceId());
        return  ApiResponse.success(invoiceInfoDetail);
    }



    /**
     * 新增发票抬头
     * @param invoiceHeaderDTO
     * @return
     */
    @Log(title = "财务模块",content = "新增发票抬头", businessType = BusinessType.INSERT)
    @ApiOperation(value = "invoiceHeaderCommit",notes = "新增发票抬头",httpMethod = "POST")
    @PostMapping("/invoiceHeaderCommit")
    public ApiResponse invoiceHeaderCommit(@RequestBody InvoiceHeaderDTO invoiceHeaderDTO){
        try {
            List<InvoiceHeaderVO> invoiceHeaderList = invoiceInfoService.queryInvoiceHeader();
            if(invoiceHeaderList.size()>0){
                invoiceInfoService.deleteInvoiceHeader();
            }
            invoiceInfoService.insertInvoiceHeader(invoiceHeaderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("新增成功");

    }
    /**
     * 发票抬头查询
     * @param
     * @return
     */
    @Log(title = "财务模块",content = "发票抬头查询", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceHeaderList",notes = "发票抬头查询",httpMethod = "POST")
    @PostMapping("/getInvoiceHeaderList")
    public ApiResponse<List<InvoiceHeaderVO>> getInvoiceHeaderList(){
        List<InvoiceHeaderVO> invoiceHeaderList = invoiceInfoService.queryInvoiceHeader();
        return ApiResponse.success(invoiceHeaderList);
    }
 }
