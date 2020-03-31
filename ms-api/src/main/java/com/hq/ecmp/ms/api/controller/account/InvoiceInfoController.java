package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.ms.api.dto.invoice.InvoiceDto;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.dto.InvoiceByTimeStateDTO;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @ApiOperation(value = "getInvoiceInfoList",notes = "发票记录列表查询",httpMethod = "POST")
    @PostMapping("/getInvoiceInfoList")
    public ApiResponse<List<InvoiceRecordVO>> getInvoiceInfoList(@RequestBody InvoiceByTimeStateDTO invoiceByTimeStateDTO){
        List<InvoiceRecordVO> invoiceInfoList = invoiceInfoService.queryAllByTimeState(invoiceByTimeStateDTO);
        return ApiResponse.success(invoiceInfoList);
    }
    /**
     * 发票信息新增
     * @param invoiceInfo
     * @return
     */
    @ApiOperation(value = "invoiceInfoCommit",notes = "新增发票信息",httpMethod = "POST")
    @PostMapping("/invoiceInfoCommit")
     public ApiResponse invoiceInfoCommit(@RequestBody InvoiceInfo invoiceInfo){
         try {
             invoiceInfoService.insertInvoiceInfo(invoiceInfo);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }


    /**
     * 发票信息详情
     * @param invoiceInfo
     * @return
     */
    @ApiOperation(value = "getInvoiceInfoDetail",notes = "发票信息详情",httpMethod ="POST")
    @PostMapping("/getInvoiceInfoDetail")
    public ApiResponse<InvoiceInfo> getInvoiceInfoDetail(InvoiceInfo invoiceInfo){
        InvoiceInfo invoiceInfoDetail = invoiceInfoService.selectInvoiceInfoById(invoiceInfo.getInvoiceId());
        return  ApiResponse.success(invoiceInfoDetail);
    }



    /**
     * 发票信息下载
     * @param invoiceInfo
     * @return
     */
    @ApiOperation(value = "getInvoiceDownLoad",notes = "发票下载",httpMethod ="POST")
    @PostMapping("/getInvoiceDownLoad")
    public ApiResponse<InvoiceInfo> getInvoiceDownLoad(InvoiceInfo invoiceInfo){
        InvoiceInfo downLoad = invoiceInfoService.selectInvoiceInfoById(invoiceInfo.getInvoiceId());
        return  ApiResponse.success(downLoad);
    }

    /**
     * 新增发票抬头
     * @param invoiceHeaderVO
     * @return
     */
    @ApiOperation(value = "invoiceHeaderCommit",notes = "新增发票抬头",httpMethod = "POST")
    @PostMapping("/invoiceHeaderCommit")
    public ApiResponse invoiceHeaderCommit(@RequestBody InvoiceHeaderVO invoiceHeaderVO){
        try {
            invoiceInfoService.insertInvoiceHeader(invoiceHeaderVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("新增成功");

    }

 }
