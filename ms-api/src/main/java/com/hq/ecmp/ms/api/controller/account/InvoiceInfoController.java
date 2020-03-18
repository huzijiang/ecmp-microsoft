package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.ms.api.dto.invoice.InvoiceDto;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
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
 @RequestMapping("/invoiceinfo")
public class InvoiceInfoController {

    @Autowired
    private IInvoiceInfoService invoiceInfoService;
    /**
     * 发票信息查询
     * @param invoiceInfo
     * @return list
     */
    @ApiOperation(value = "getInvoiceInfoList",notes = "查询所有的发票详细信息",httpMethod = "POST")
    @PostMapping("/getInvoiceInfoList")
    public ApiResponse<List<InvoiceInfo>> getInvoiceInfoList(InvoiceInfo invoiceInfo){
        List<InvoiceInfo> invoiceInfoList = invoiceInfoService.selectInvoiceInfoList(invoiceInfo);
        return ApiResponse.success();
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
     * 发票信息修改
     * @param invoiceInfo
     * @return
     */
     @ApiOperation(value = "invoiceInfoUpdate",notes = "修改发票信息",httpMethod = "POST")
     @PostMapping("/invoiceInfoUpdate")
     public ApiResponse invoiceInfoUpdate(@RequestBody InvoiceInfo invoiceInfo){

         try {
             invoiceInfoService.updateInvoiceInfo(invoiceInfo);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 发票信息删除
     * @param invoiceInfo
     * @return
     */
     @ApiOperation(value = "invoiceInfoDelete",notes = "删除发票信息",httpMethod = "POST")
     @PostMapping("/invoiceInfoDelete")
     public ApiResponse invoiceInfoDelete(@RequestBody InvoiceInfo invoiceInfo){
         try {
             invoiceInfoService.deleteInvoiceInfoById(invoiceInfo.getInvoiceId());
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除成功");
     }
    /**
     * 发票信息详情
     * @param invoiceInfo
     * @return
     */
    @ApiOperation(value = "getinvoiceInfoDetail",notes = "发票信息详情",httpMethod ="POST")
    @PostMapping("/getinvoiceInfoDetail")
    public ApiResponse<InvoiceInfo> getinvoiceInfoDetail(InvoiceInfo invoiceInfo){
        InvoiceInfo nvoiceInfoDetail = invoiceInfoService.selectInvoiceInfoById(invoiceInfo.getInvoiceId());
        return  ApiResponse.success(nvoiceInfoDetail);
    }

    /**
     * 根据时间区间、开票状态查询发票信息
     * @param invoiceDto
     * @return
     */
    @ApiOperation(value = "getinvoiceByTimeAndState",notes = "发票信息详情",httpMethod ="POST")
    @PostMapping("/getinvoiceByTimeAndState")
    public ApiResponse<List<InvoiceInfo>>  getinvoiceByTimeAndState(InvoiceDto invoiceDto){
        List<InvoiceInfo>  invoiceInfoList = invoiceInfoService.selectInvoiceInfoByTimeAndState(invoiceDto.getStartTime(),invoiceDto.getEndTime(),invoiceDto.getState());
        return ApiResponse.success(invoiceInfoList);
    }


    /**
     * 发票信息下载
     * @param invoiceInfo
     * @return
     */
    @ApiOperation(value = "getinvoiceInfodownLoad",notes = "发票下载",httpMethod ="POST")
    @PostMapping("/getinvoiceInfodownLoad")
    public ApiResponse<InvoiceInfo> getinvoiceInfodownLoad(InvoiceInfo invoiceInfo){
        InvoiceInfo nvoiceInfodownLoad = invoiceInfoService.selectInvoiceInfoById(invoiceInfo.getInvoiceId());
        return  ApiResponse.success(nvoiceInfodownLoad);
    }



 }
