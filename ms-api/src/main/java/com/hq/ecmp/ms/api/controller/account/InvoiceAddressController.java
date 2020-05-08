package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;

import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.InvoiceAddUpdateDTO;
import com.hq.ecmp.mscore.dto.InvoiceAddressDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import com.hq.ecmp.mscore.vo.InvoiceAddVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private TokenService tokenService;

    /**
     * 发票地址查询-查询所有的发票地址
     * @param
     * @return list
     */
    @Log(title = "财务模块",content = "删除邮箱信息", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceAddressList",notes = "查询所有的发票地址信息",httpMethod = "POST")
    @PostMapping("/getInvoiceAddressList")
    public ApiResponse<PageResult<InvoiceAddVO>> getInvoiceAddressList(@RequestBody PageRequest pageRequest){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        PageResult<InvoiceAddVO> invoiceAddressList = invoiceAddressService.selectInvoiceAddressList(pageRequest,companyId);
        return ApiResponse.success(invoiceAddressList);
    }
    /**
     * 发票地址新增
     * @param invoiceAddressDTO
     * @return
     */
    @Log(title = "财务模块", content = "新增发票地址信息",businessType = BusinessType.INSERT)
    @ApiOperation(value = "invoiceAddCommit",notes = "新增发票地址信息",httpMethod = "POST")
    @PostMapping("/invoiceAddCommit")
     public ApiResponse invoiceAddCommit(@RequestBody InvoiceAddressDTO invoiceAddressDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        invoiceAddressDTO.setCompanyId(companyId);
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
     * @param invoiceAddUpdateDTO
     * @return
     */
    @Log(title = "财务模块",content = "修改发票地址信息", businessType = BusinessType.UPDATE)
     @ApiOperation(value = "invoiceAddUpdate",notes = "修改发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddUpdate")
     public ApiResponse invoiceAddUpdate(@RequestBody InvoiceAddUpdateDTO invoiceAddUpdateDTO){

         try {
             invoiceAddressService.updateInvoiceAddress(invoiceAddUpdateDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 发票地址删除
     * @param invoiceAddUpdateDTO
     * @return
     */
    @Log(title = "财务模块", content = "删除发票地址信息",businessType = BusinessType.DELETE)
     @ApiOperation(value = "invoiceAddDelete",notes = "删除发票地址信息",httpMethod = "POST")
     @PostMapping("/invoiceAddDelete")
     public ApiResponse invoiceAddDelete(@RequestBody InvoiceAddUpdateDTO invoiceAddUpdateDTO){
         try {
             invoiceAddressService.deleteInvoiceAddressById(invoiceAddUpdateDTO.getAddressId());
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除成功");
     }
}
