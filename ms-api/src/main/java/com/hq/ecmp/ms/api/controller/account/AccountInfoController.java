package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.domain.OrderAccountInfo;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.service.IOrderAccountInfoService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
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
 @RequestMapping("/accountinfo")
public class AccountInfoController {

    @Autowired
    private IEcmpOrgService iEcmpOrgService;
    @Autowired
    private IOrderAccountInfoService iOrderAccountInfoService;
    @Autowired
    private IOrderSettlingInfoService OrderSettlingInfoService;

    /**
     * 网约车账务订单总览查询i
     * @param rderAccountInfo
     * @return list
     */
    @ApiOperation(value = "getOrderAccountList",notes = "查询账务订单信息",httpMethod = "POST")
    @PostMapping("/getOrderAccountList")
    public ApiResponse<List<OrderAccountInfo>> getOrderAccountList(OrderAccountInfo rderAccountInfo){
        List<OrderAccountInfo> invoiceInfoList = iOrderAccountInfoService.selectOrderAccountInfoList(rderAccountInfo);
        return ApiResponse.success();
    }
    /**
     *  账单下载
     * @param
     * @return
     */



}
