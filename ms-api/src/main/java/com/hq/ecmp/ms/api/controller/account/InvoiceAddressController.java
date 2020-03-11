package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
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
 @RequestMapping("/invoiceAddress")
public class InvoiceAddressController {

    @Autowired
    private IInvoiceAddressService invoiceAddressService;
    /**
     * 发票地址查询-查询所有的发票地址
     * @param invoiceAddress
     * @return list
     */
    @ApiOperation(value = "getInvoiceAddressList",notes = "查询所有的发票地址信息",httpMethod = "POST")
    @PostMapping("/getInvoiceAddressList")
    public ApiResponse<List<InvoiceAddress>> getInvoiceAddressList(InvoiceAddress invoiceAddress){
        List<InvoiceAddress> invoiceAddressList = invoiceAddressService.selectInvoiceAddressList(invoiceAddress);
        return ApiResponse.success();
    }
    /**
     * 发票地址新增
     * @param invoiceAddress
     * @return
     */
    @ApiOperation(value = "invoiceAddCommit",notes = "新增发票地址信息",httpMethod = "POST")
    @PostMapping("/invoiceAddCommit")
     public ApiResponse invoiceAddCommit(@RequestBody InvoiceAddress invoiceAddress){
         try {
             invoiceAddressService.insertInvoiceAddress(invoiceAddress);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }
    /**
     * 发票地址修改
     * @param invoiceAddress
     * @return
     */
     @ApiOperation(value = "invoiceAddUpdate",notes = "修改发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddUpdate")
     public ApiResponse invoiceAddUpdate(@RequestBody InvoiceAddress invoiceAddress){

         try {
             invoiceAddressService.updateInvoiceAddress(invoiceAddress);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 发票地址删除
     * @param invoiceAddress
     * @return
     */
     @ApiOperation(value = "invoiceAddDelete",notes = "删除发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddDelete")
     public ApiResponse invoiceAddDelete(@RequestBody InvoiceAddress invoiceAddress){
         try {
             invoiceAddressService.deleteInvoiceAddressById(invoiceAddress.getAddressId());
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除成功");
     }
}
