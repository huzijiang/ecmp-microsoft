package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;

import com.hq.ecmp.mscore.dto.InvoiceAddressDTO;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import com.hq.ecmp.mscore.vo.InvoiceAddVO;
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
 @RequestMapping("/invoiceAdd")
public class InvoiceAddressController {

    @Autowired
    private IInvoiceAddressService invoiceAddressService;
    /**
     * 发票地址查询-查询所有的发票地址
     * @param
     * @return list
     */
    @ApiOperation(value = "getInvoiceAddressList",notes = "查询所有的发票地址信息",httpMethod = "POST")
    @PostMapping("/getInvoiceAddressList")
    public ApiResponse<List<InvoiceAddVO>> getInvoiceAddressList(){
        List<InvoiceAddVO> invoiceAddressList = invoiceAddressService.selectInvoiceAddressList();
        return ApiResponse.success(invoiceAddressList);
    }
    /**
     * 发票地址新增
     * @param invoiceAddressDTO
     * @return
     */
    @ApiOperation(value = "invoiceAddCommit",notes = "新增发票地址信息",httpMethod = "POST")
    @PostMapping("/invoiceAddCommit")
     public ApiResponse invoiceAddCommit(@RequestBody InvoiceAddressDTO invoiceAddressDTO){
         try {
             invoiceAddressService.insertInvoiceAddress(invoiceAddressDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }
    /**
     * 发票地址修改
     * @param invoiceAddressDTO
     * @return
     */
     @ApiOperation(value = "invoiceAddUpdate",notes = "修改发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddUpdate")
     public ApiResponse invoiceAddUpdate(@RequestBody InvoiceAddressDTO invoiceAddressDTO){

         try {
             invoiceAddressService.updateInvoiceAddress(invoiceAddressDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 发票地址删除
     * @param addressId
     * @return
     */
     @ApiOperation(value = "invoiceAddDelete",notes = "删除发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddDelete")
     public ApiResponse invoiceAddDelete(Long addressId){
         try {
             invoiceAddressService.deleteInvoiceAddressById(addressId);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除成功");
     }
}
