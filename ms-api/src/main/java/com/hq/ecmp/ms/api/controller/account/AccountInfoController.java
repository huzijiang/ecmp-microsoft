package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.domain.OrderAccountInfo;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.service.IOrderAccountInfoService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import com.hq.ecmp.mscore.vo.OrderAccountVO;
import com.hq.ecmp.mscore.vo.OrderAccountViewVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账务信息
 * @author shixin
 * @date 2020-3-7
 *
 */
 @RestController
 @RequestMapping("/account")
public class AccountInfoController {

    @Autowired
    private IEcmpOrgService iEcmpOrgService;
    @Autowired
    private IOrderAccountInfoService iOrderAccountInfoService;
    @Autowired
    private IOrderSettlingInfoService OrderSettlingInfoService;

    /**
     * 网约车账务订单总览查询
     * @param
     * @return list
     */
    @ApiOperation(value = "getAccountViewList",notes = "查询账务订单信息",httpMethod = "POST")
    @PostMapping("/getAccountViewList")
    public ApiResponse<List<OrderAccountViewVO>> getAccountViewList(){
        List<OrderAccountViewVO> invoiceViewList = iOrderAccountInfoService.getAccountViewList();
        return ApiResponse.success(invoiceViewList);
    }
    /**
     * 开发票下拉列表
     * @param
     * @return list
     */
    @ApiOperation(value = "getAccountList",notes = "获取为开发票的订单统计列表",httpMethod = "POST")
    @PostMapping("/getAccountList")
    public ApiResponse<List<OrderAccountVO>> getAccountList(){
        List<OrderAccountVO> invoiceInfoList = iOrderAccountInfoService.getAccountList();
        return ApiResponse.success(invoiceInfoList);
    }




}
